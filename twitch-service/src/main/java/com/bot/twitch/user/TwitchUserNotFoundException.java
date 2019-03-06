package com.bot.twitch.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TwitchUserNotFoundException extends RuntimeException {
	
	public TwitchUserNotFoundException(String message) {
		super(message);
	}
}
