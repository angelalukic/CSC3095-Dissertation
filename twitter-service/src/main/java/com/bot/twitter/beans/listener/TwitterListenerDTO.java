package com.bot.twitter.beans.listener;

import java.util.Set;

import com.bot.discord.beans.server.DiscordServer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterListenerDTO {
	
	private long id;
	private String name;
	private Set<DiscordServer> servers;
	
	public TwitterListenerDTO() {
	}
	
	public TwitterListenerDTO(TwitterListener listener) {
		this.id = listener.getId();
		this.name = listener.getName();
		this.servers = listener.getServers();
	}
}
