package com.bot.discord.server;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.bot.h2.Subscription;
import com.bot.twitter.listener.TwitterListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

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
