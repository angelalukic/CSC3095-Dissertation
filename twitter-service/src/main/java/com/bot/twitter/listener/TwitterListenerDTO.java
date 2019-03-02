package com.bot.twitter.listener;

import java.util.Set;

import com.bot.discord.server.DiscordServer;

import lombok.Getter;
import lombok.Setter;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class TwitterListenerDTO {
	
	private long id;
	private String name;	
	private Set<DiscordServer> servers;
	
	public TwitterListenerDTO() {
	}

	public TwitterListenerDTO(long id, String name, Set<DiscordServer> servers) {
		this.id = id;
		this.name = name;
		this.servers = servers;
	}
}
