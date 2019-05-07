package com.bot.twitch.beans;

import com.bot.discord.beans.server.DiscordServer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchSubscription {
	
	private TwitchListener listener;
	private DiscordServer server;
	
	public TwitchSubscription(TwitchListener listener, DiscordServer server) {
		this.listener = listener;
		this.server = server;
	}
}
