package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchUser;

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
	
	public TwitchChatMessage(TwitchUser channel, TwitchUser user, String message) {
		this.channel = channel;
		this.user = user;
		this.message = message;
	}
}
