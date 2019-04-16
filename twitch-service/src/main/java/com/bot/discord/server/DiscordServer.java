package com.bot.discord.server;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bot.twitch.listener.TwitchListener;
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
	private Set<TwitchListener> listeners;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, Set<TwitchListener> listeners) {
		this.id = id;
		this.name = name;
		this.listeners = listeners;
	}
	
	public DiscordServer(DiscordServerDTO discordServer) {
		this.id = discordServer.getId();
		this.name = discordServer.getName();
		this.listeners = discordServer.getListeners();
	}
}