package com.bot.discord.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordMessage {
	
	private long id;
	private String message;
	
	public DiscordMessage(long id, String message) {
		this.id = id;
		this.message = message;
	}
}
