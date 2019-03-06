package com.bot.discord.server;

import java.util.Set;

import com.bot.twitch.listener.TwitchListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordServerDTO {
	
	private long id;
	private String name;
	private Set<TwitchListener> listeners;
	
	public DiscordServerDTO() {
	}

	public DiscordServerDTO(long id, String name, Set<TwitchListener> listeners) {
		this.id = id;
		this.name = name;
		this.listeners = listeners;
	}
}
