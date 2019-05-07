package com.bot.discord.beans.server;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DiscordServer {
	
	@Id	private long id;
	private String name;
	private long notificationChannel;
	private long adminChannel;
	private long reportChannel;
	private long twitchLogChannel;
	private long serverJoinChannel;
	private String serverJoinMessage;
	private String roleColour;
	private boolean aiEnabled;
	private long aiChannel;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, long notificationChannel, long adminChannel, long reportChannel, long twitchLogChannel, 
			long serverJoinChannel, String serverJoinMessage, String roleColour, boolean aiEnabled, long aiChannel) {
		this.id = id;
		this.name = name;
		this.notificationChannel = notificationChannel;
		this.adminChannel = adminChannel;
		this.reportChannel = reportChannel;
		this.twitchLogChannel = twitchLogChannel;
		this.serverJoinChannel = serverJoinChannel;
		this.serverJoinMessage = serverJoinMessage;
		this.roleColour = roleColour;
		this.aiEnabled = aiEnabled;
		this.aiChannel = aiChannel;
	}
}
