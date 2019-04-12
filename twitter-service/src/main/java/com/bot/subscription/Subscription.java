package com.bot.subscription;

import com.bot.discord.server.DiscordServer;
import com.bot.twitter.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription {
	
	private DiscordServer server;
	private TwitterListener listener;
	
	public Subscription() {
	}
	
	public Subscription(DiscordServer server, TwitterListener listener) {
		this.server = server;
		this.listener = listener;
	}
}
