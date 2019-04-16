package com.bot.twitch.listener;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bot.discord.server.DiscordServer;
import com.bot.twitter.listener.TwitterListener;
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
public class TwitchListener {
	
	@Id
	private long id;
	
	private String name;

	@ManyToMany(mappedBy = "twitchListeners")
	@JsonIgnore
	private Set<DiscordServer> servers;
	
	@ManyToMany(mappedBy = "twitchListeners")
	@JsonIgnore
	private Set<TwitterListener> twitterListeners;
	
	public TwitchListener() {
	}

	public TwitchListener(long id, String name, Set<DiscordServer> servers, Set<TwitterListener> twitterListener) {
		this.id = id;
		this.name = name;
		this.servers = servers;
	}
}
