package com.wowserman.listeners;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;

import com.comphenix.protocol.wrappers.EnumWrappers.ChatType;
import com.wowserman.EnhancedChat;
import com.wowserman.api.PopulateKeywordEvent;
import com.wowserman.api.SearchForKeywordEvent;
import com.wowserman.settings.Settings;

public class ChatListener extends PacketAdapter {

	public ChatListener(Plugin plugin) {
		super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Server.CHAT);
		ProtocolLibrary.getProtocolManager().addPacketListener(this);
	}

	@Override
	public void onPacketSending(PacketEvent event) {

		PacketContainer packet = event.getPacket();
		StructureModifier<WrappedChatComponent> chatComponents = packet.getChatComponents();

		if (packet.getChatTypes().getValues().get(0) == ChatType.GAME_INFO)
			return;

		WrappedChatComponent msg = chatComponents.read(0);

		TextComponent original = new TextComponent(ComponentSerializer.parse(msg.getJson()));

		if (EnhancedChat.shouldIgnore(original))
			return;

		final long startTime = System.currentTimeMillis();

		List<String> list = new ArrayList<String>();

		this.enhance(original, event.getPlayer());

		list.clear();

		if (Settings.isDebug())
			this.plugin.getLogger()
					.info("Finished Formatting Message in " + (System.currentTimeMillis() - startTime) + "ms.");

		msg.setJson(ComponentSerializer.toString(original));

		chatComponents.write(0, msg);

	}

	@SuppressWarnings("deprecation")
	public void enhance(BaseComponent component, Player context) {

		if (component instanceof TextComponent) {
			TextComponent tComponent = (TextComponent) component;

			final String text = tComponent.getText();

			SearchForKeywordEvent search = new SearchForKeywordEvent(text, context.getName());

			Bukkit.getPluginManager().callEvent(search);

			if (text != null && search.hasFoundKeyword() && !search.isOnlyKeyword()) {

				int index = text.toLowerCase().indexOf(search.getFoundKeyword().toLowerCase());

				String first = index != 0 ? text.substring(0, index) : null;

				String cut = text.substring(index, index + search.getFoundKeyword().length());

				String last = cut.length() + index <= text.length()
						? text.substring(cut.length() + index, text.length())
						: null;

				if (first == null) {
					tComponent.setText(cut);

					tComponent.addExtra(last);
				} else {
					tComponent.setText(first);

					tComponent.addExtra(cut);
					tComponent.addExtra(last);
				}
			}

			PopulateKeywordEvent populate = new PopulateKeywordEvent(tComponent.getText(), search.getContext(),
					search.getID());

			Bukkit.getPluginManager().callEvent(populate);

			if (Settings.isDebug())
				this.plugin.getLogger()
						.info("" + tComponent.getText() + "| Description: "
								+ String.join(", ", populate.getDescription()) + "|" + "Commands: "
								+ String.join(", ", populate.getCommands()) + "|" + "URL: " + populate.getURL());

			if (!populate.getCommands().isEmpty()) {
				tComponent.setClickEvent(new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
						String.join("&&", EnhancedChat.colorify(populate.getCommands()))));
			}

			if (!populate.getDescription().isEmpty()) {
				tComponent.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(String.join("\n", EnhancedChat.colorify(populate.getDescription())))
								.create()));
			}

			if (populate.hasURL()) {
				tComponent.setClickEvent(
						new ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.OPEN_URL, populate.getURL()));
			}

			if (populate.hasInsertion()) {
				tComponent.setInsertion(populate.getInsertion());
			}
		}

		if (component.getExtra() != null) {

			for (BaseComponent extra : component.getExtra())
				this.enhance(extra, context);
		}
	}
}
