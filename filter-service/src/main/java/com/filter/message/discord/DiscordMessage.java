package com.filter.message.discord;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordMessage {
	
	private long id;
	private String message;
	
	public DiscordMessage() {
	}
	
	public DiscordMessage(DiscordMessage message) {
		this.id = message.getId();
		this.message = message.getMessage();
	}
}
