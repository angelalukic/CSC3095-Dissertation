package com.bot.h2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bot.discord.server.DiscordServer;
import com.bot.twitter.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Subscription {
	
	@Id
	@GeneratedValue
	private int id;
	
	@ManyToOne
	@JoinColumn(name="discord_id")
	private DiscordServer server;
	
	@ManyToOne
	@JoinColumn(name="twitter_id")
	private TwitterListener listener;
	
	public Subscription() {
	}

	public Subscription(int id, DiscordServer server, TwitterListener listener) {
		this.id = id;
		this.server = server;
		this.listener = listener;
	}
}
