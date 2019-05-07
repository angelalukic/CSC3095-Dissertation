package com.bot.discord.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordException {

	private Date timestamp;
	private String message;
	private String details;
	
	public DiscordException(Date timestamp, String message, String details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}
}
