package com.filter.message.twitch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchMessage {
	
	private long id;
	private String message;
	
	public TwitchMessage() {
	}
	
	public TwitchMessage(TwitchMessage message) {
		this.id = message.getId();
		this.message = message.getMessage();
	}
}
