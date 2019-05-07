package com.bot.discord.beans.server;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bot.twitch.beans.listener.TwitchListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
@Entity
public class DiscordServer {
	
	@Id
	private long id;
	private String name;
	
	@ManyToMany(mappedBy = "servers")
	@JsonIgnore
	private Set<TwitchListener> twitchListeners;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, Set<TwitchListener> twitchListeners) {
		this.id = id;
		this.name = name;
		this.twitchListeners = twitchListeners;
	}
}
