#### Enhanced Chat
> A Chat Interceptor API for splicing and editting outgoing server chat messages based on specifc keywords.

#### Why would plugins use this
```
1. Plugins would be able to add their own functionality to chat.
2. Plugins would not be required to implement their own packet interceptor.
3. Plugins would not need to handle TextComponents on their own.
```
##### How to implement
The API usage is built on the Bukkit Event API, meaning plugins that use the API don't have to "Hook" into it, they just use the Bukkit Event API to listen to the API's events.

The two events that are triggered (and what plugins will listen for) are:
- SearchForKeywordEvent
Which represents when the API searches for a keyword in a message, plugins use this event to specify what keywords to look for,
and what the keywords represent.

- PopulateKeywordEvent
When a keyword is found, this event is called to "ask" plugins what the keyword's hover-tips, commands, insertion, and url are.

###### Example
We want to add a hover-tip to every player's name in chat. We first need to make an **ID** to classify keywords as player names.

`final long PLAYER_NAME_KEYWORD_ID = KeywordManager.createID("player-names");`

We'll use this to tag our keywords we find in *SearchForKeywordEvent* so we don't have to recheck in *PopulateKeywordEvent*

Next, make a class that implements *Listener*, this is where we'll handle *SearchForKeywordEvent* and *PopulateKeywordEvent*.

I called our *Listener* class `KeywordListener`, you can call it whatever you'd like. Now inside `KeywordListener`, create two methods where one passes a argument that is a *SearchForKeywordEvent*, and one that is *PopulateKeywordEvent*, then add the *EventHandler* annotation to both of them.

```
@EventHandler
public void search(SearchForKeywordEvent event) {

}

@EventHandler
public void populate(PopulateKeywordEvent event) {

}
```

Now that we have that set up, we want to work inside our *SearchForKeywordEvent* *EventHandler* method.

We're checking if the message contains any player's username. and if it does: set the event's keyword to the player's name, then set the id of the event to our  *long* `PLAYER_NAME_KEYWORD_ID`, then because we're talking about a player, our context would be that player, so we can just set the event's context as the player's name. To do that, it'd look something like this:
```
for (Player player:Bukkit.getOnlinePlayers()) {
	if (event.containsCustomKeyword(player.getName())) {
		// Message contains a player's name. 
		// We need to say what the keyword is that we found.
		// We're saying what word in the message we identified as a keyword.
		event.setKeyword(player.getName());
		// We need to say what the keyword classifies as.
		// In other words, we're saying the keyword describes a Player's Name.
		event.setID(PLAYER_NAME_KEYWORD_ID); 
		// We need to say what we are talking about.
		// In this case we know we found a keyword that identifies a Player,
		// So now we're saying it's a Player named player.getName().
		event.setContext(player.getName());
	}
}
```

Now our SearchForKeywordEvent handler is done. Let's move onto our PopulateKeywordEvent handler.

Know that first the API gets a message, then triggers a *SearchForKeywordEvent*, and if the *SearchForKeywordEvent* finds a keyword after letting plugins handle it, it will then try to populate it by triggering a *PopulateKeywordEvent* about the keyword it found in *SearchForKeywordEvent*. Populating it means to give it commands to run when clicked, a hover-tip to show, insertion to add, and or a url to open for a keyword.

We want to populate Player Name keywords so when clicked, the clicker runs the command `/tpa (player name)` where (player name) is replaced by the context of the keyword. Our method body of our PopulateKeywordEvent handler should look like this:
```
// Check if the Keyword's ID is classified as a Player's Username
if (event.equalsID(PLAYER_NAME_KEYWORD_ID )) {
	// The Keyword represents a Player's Username.
	// The Context should be a Player's name,
	// according to what we did in our SearchForKeywordEvent handler.
	// 
	// In commands of our PLAYER_NAME_KEYWORD_ID, 
	// we can use the placeholder '%context%' and it will replace to our context
	// when executed.
	// So the command would look something like this "/tpa %context%"
	// We could also just do "/tpa " + event.getContext();
	// Either one works, we're just using "/tpa %context%" as it's built into the API.
	event.addCommand("/tpa %context%");
}
```






