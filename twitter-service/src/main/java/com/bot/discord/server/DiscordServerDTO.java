package com.bot.discord.server;

import java.util.Set;

import com.bot.twitter.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class DiscordServerDTO {
	
	private long id;
	private String name;
	private Set<TwitterListener> listeners;
	
	public DiscordServerDTO() {
	}

	public DiscordServerDTO(long id, String name, Set<TwitterListener> listeners) {
		this.id = id;
		this.name = name;
		this.listeners = listeners;
	}
}
