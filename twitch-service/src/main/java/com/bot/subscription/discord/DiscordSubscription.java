package com.bot.subscription.discord;

import com.bot.discord.server.DiscordServer;
import com.bot.twitch.listener.TwitchListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordSubscription {
	
	private DiscordServer server;
	private TwitchListener listener;
	
	public DiscordSubscription() {
	}

	public DiscordSubscription(DiscordServer server, TwitchListener listener) {
		this.server = server;
		this.listener = listener;
	}
}
