package com.bot.twitch.subscription;

import com.bot.discord.server.DiscordServer;
import com.bot.twitch.TwitchListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchDiscordSubscription {
	
	private TwitchListener listener;
	private DiscordServer server;
	
	public TwitchDiscordSubscription(TwitchListener listener, DiscordServer server) {
		this.listener = listener;
		this.server = server;
	}
}
