package com.filter.message.discord;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class DiscordServerNotFoundException extends RuntimeException {
	
	public DiscordServerNotFoundException(String message) {
		super(message);
	}
}
