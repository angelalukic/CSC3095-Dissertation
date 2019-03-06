package com.bot.twitch.listener;

import java.util.Set;

import com.bot.discord.server.DiscordServer;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class TwitchListenerDTO {
	
	private long id;
	private String name;	
	private Set<DiscordServer> servers;
	
	public TwitchListenerDTO() {
	}

	public TwitchListenerDTO(long id, String name, Set<DiscordServer> servers) {
		this.id = id;
		this.name = name;
		this.servers = servers;
	}
}
