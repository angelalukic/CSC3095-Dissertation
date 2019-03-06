package com.bot.twitch.listener;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TwitchListenerNotFoundException extends RuntimeException {
	
	public TwitchListenerNotFoundException(String message) {
		super(message);
	}
}
