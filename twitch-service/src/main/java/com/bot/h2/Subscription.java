package com.bot.h2;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.bot.discord.server.DiscordServer;
import com.bot.twitch.listener.TwitchListener;

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
	@JoinColumn(name="twitch_id")
	private TwitchListener listener;
	
	public Subscription() {
	}

	public Subscription(int id, DiscordServer server, TwitchListener listener) {
		this.id = id;
		this.server = server;
		this.listener = listener;
	}
}
