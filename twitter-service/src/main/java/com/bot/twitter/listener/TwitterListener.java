package com.bot.twitter.listener;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.bot.discord.server.DiscordServer;
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
public class TwitterListener {
	
	@Id
	private long id;
	
	private String name;	
	
	@ManyToMany
	@JoinTable(
			name = "subscription",
			joinColumns = @JoinColumn(name = "twitter_id"),
			inverseJoinColumns = @JoinColumn(name = "discord_id"))
	@JsonIgnore
	private Set<DiscordServer> servers;
	
	public TwitterListener() {
	}

	public TwitterListener(long id, String name, Set<DiscordServer> servers) {
		this.id = id;
		this.name = name;
		this.servers = servers;
	}
}
