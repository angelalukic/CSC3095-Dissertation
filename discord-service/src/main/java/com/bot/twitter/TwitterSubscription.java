package com.bot.twitter;

import com.bot.discord.server.DiscordServerDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterSubscription {
	
	private DiscordServerDTO server;
	private TwitterListener listener;
	
	public TwitterSubscription() {
	}

	public TwitterSubscription(TwitterListener listener, DiscordServerDTO server) {
		this.listener = listener;
		this.server = server;
	}
}
