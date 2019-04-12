package com.bot.discord.server;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DiscordServer {
	
	@Id
	private long id;
	
	private String name;
	private long notificationChannel;
	private long adminChannel;
	private long reportChannel;
	private long twitchLogChannel;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, long notificationChannel, long adminChannel, long reportChannel, long twitchLogChannel) {
		this.id = id;
		this.name = name;
		this.notificationChannel = notificationChannel;
		this.adminChannel = adminChannel;
		this.reportChannel = reportChannel;
		this.twitchLogChannel = twitchLogChannel;
	}
}
