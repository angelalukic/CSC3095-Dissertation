package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchUser;
import com.github.twitch4j.helix.domain.User;

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
	
	public TwitchChatMessage(User channel, User user, String message) {
		this.channel = new TwitchUser(channel);
		this.user = new TwitchUser(user);
		this.message = message;
	}
}
