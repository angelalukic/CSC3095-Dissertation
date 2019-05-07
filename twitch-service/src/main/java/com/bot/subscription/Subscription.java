package com.bot.subscription;

import com.bot.discord.beans.server.DiscordServer;
import com.bot.twitch.beans.listener.TwitchListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Subscription {
	
	private TwitchListener listener;
	private DiscordServer server;
	
	public Subscription() {
	}

	public Subscription(TwitchListener listener, DiscordServer server) {
		this.listener = listener;
		this.server = server;
	}
}
