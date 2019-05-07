package com.bot.discord.beans.server;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bot.twitter.beans.listener.TwitterListener;
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
	private Set<TwitterListener> listeners;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, Set<TwitterListener> listeners) {
		this.id = id;
		this.name = name;
		this.listeners = listeners;
	}
}
