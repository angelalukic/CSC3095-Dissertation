package com.bot.twitch.features.beans;

import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchChatMessage {
	
	private TwitchUser channel;
	private TwitchUser user;
	private String message;
	
	public TwitchChatMessage() {
	}
	
	public TwitchChatMessage(ChannelMessageEvent event, TwitchClient client) {
		this.channel = new TwitchUser(event.getChannel(), client);
		this.user = new TwitchUser(event.getUser(), client);
		this.message = event.getMessage();
	}
}
