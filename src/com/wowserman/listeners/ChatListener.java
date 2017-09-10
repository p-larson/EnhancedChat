package com.wowserman.listeners;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.wowserman.EnhancedChat;
import com.wowserman.api.PopulateKeywordEvent;
import com.wowserman.api.SearchForKeywordEvent;

public class ChatListener extends PacketAdapter {

	public ChatListener(Plugin plugin) {
		super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT);
	}
	
	@Override
	public void onPacketSending(PacketEvent event) {
		
		PacketContainer packet = event.getPacket();
		StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();

		if (packet.getChatTypes().getValues().get(0) == ChatType.GAME_INFO)
			return;

		WrappedChatComponent msg = chatComponents.read(0);

		TextComponent original = new TextComponent(ComponentSerializer.parse(msg.getJson()));

		final String fullMessage = original.toLegacyText();
				
		if (EnhancedChat.shouldIgnore(original))
			return;
		
		final long startTime = System.currentTimeMillis();
		
		List<TextComponent> list = new ArrayList<TextComponent>();
		
		List<String> splits = new ArrayList<String>();
		
		HashMap<String, SearchForKeywordEvent> map = new HashMap<String, SearchForKeywordEvent>();
		
		splits.addAll(Arrays.asList(fullMessage.split(String.valueOf(ChatColor.COLOR_CHAR))));
		
		for (int index = 0; index < splits.size(); index++)
			splits.set(index, String.valueOf(ChatColor.COLOR_CHAR) + splits.get(index));
		
		ChatColor lastColor = null;
		ChatColor lastFormat = null;
		
		int size = splits.size();
		
		for (int i = 0; i < size; i++) {
			String split = splits.get(i);
			
			if ((split.length()==1 && split.charAt(0)==ChatColor.COLOR_CHAR) || split.length()==0) {
				splits.remove(i);
				size = splits.size();
				i--;
				continue;
			}
			
			if (split.length()>=2 && split.startsWith(String.valueOf(ChatColor.COLOR_CHAR))) {
				ChatColor color = ChatColor.getByChar(split.charAt(1));
				
				if (EnhancedChat.isColor(color)) {
					lastColor = color;
					lastFormat = null;
				} else lastFormat = color;
				
				if (split.length()==2) {
					splits.remove(i);
					size = splits.size();
					i--;
					continue;
				} else {
					split = split.substring(2, split.length());
				}
				
			}
			
			SearchForKeywordEvent search = new SearchForKeywordEvent(split, event.getPlayer());
			
			Bukkit.getPluginManager().callEvent(search);
			
			if (search.hasFoundKeyword() && search.getFoundKeyword().length() != split.length()) {
				
				map.put(search.getFoundKeyword(), search);
				
				int index = split.toLowerCase().indexOf(search.getFoundKeyword().toLowerCase());
				
				String cut = split.substring(index, index + search.getFoundKeyword().length());
				
				List<String> remainders = new ArrayList<String>();
				
				if (index!=0) {
					remainders.add(split.substring(0, index));
				}
				remainders.add(cut);
				
				if (cut.length() + index <= split.length())
					remainders.add(
							split.substring
							(cut.length() + index, 
									split.length()));

				List<String> newSplits = new ArrayList<String>();
				
				newSplits.addAll(splits.subList(0, i));

				newSplits.addAll(remainders);
				
				newSplits.addAll(splits.subList(i + 1, splits.size()));
				
				size = newSplits.size();
				
				splits = newSplits;
				
				split = cut;
				
				i--;
				
				continue;
			} else if (split.length()==0 || split==null)
				continue;
						
			TextComponent component = new TextComponent(split);
			
			if (lastFormat != null) {
				if (lastFormat==ChatColor.BOLD)
					component.setBold(true);
				else if (lastFormat==ChatColor.ITALIC)
					component.setItalic(true);
				else if (lastFormat==ChatColor.STRIKETHROUGH)
					component.setItalic(true);
				else if (lastFormat==ChatColor.MAGIC)
					component.setObfuscated(true);
				else if (lastFormat==ChatColor.UNDERLINE)
					component.setUnderlined(true);
				else component.setColor(lastFormat);
			}
			
			if (lastColor != null) {
				if (lastColor==ChatColor.BOLD)
					component.setBold(true);
				else if (lastColor==ChatColor.ITALIC)
					component.setItalic(true);
				else if (lastColor==ChatColor.STRIKETHROUGH)
					component.setItalic(true);
				else if (lastColor==ChatColor.MAGIC)
					component.setObfuscated(true);
				else if (lastColor==ChatColor.UNDERLINE)
					component.setUnderlined(true);
				else component.setColor(lastColor);
			}
			
			list.add(component);
		}
				
		TextComponent component = new TextComponent("");
				
		for (TextComponent subComponent:list) {
						
			final SearchForKeywordEvent search = map.get(subComponent.getText());
			
			if (search!=null) {
				PopulateKeywordEvent populate = new PopulateKeywordEvent(search.getFoundKeyword(), search.getContext());
				
				Bukkit.getPluginManager().callEvent(populate);
				
				if (!populate.getCommands().isEmpty())
					subComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
							String.join("&&", populate.getDescription())));
				
				if (!populate.getDescription().isEmpty())
					subComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder(String.join("\n", populate.getDescription())).create()));
			}

			component.addExtra(subComponent);
		}
		
		System.out.print("Finished Formatting Message in " + (System.currentTimeMillis()-startTime) + "ms.");

		msg.setJson(ComponentSerializer.toString(component));

		chatComponents.write(0, msg);
	}
}
