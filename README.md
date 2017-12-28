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

I called our *Listener* class KeywordListener, you can call it whatever you'd like. Now inside KeywordListener, create two methods where one passes a argument that is a *SearchForKeywordEvent*, and one that is *PopulateKeywordEvent*
