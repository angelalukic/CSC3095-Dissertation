package com.bot.twitch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TwitchStreamNotFoundException extends RuntimeException {
	
	public TwitchStreamNotFoundException(String message) {
		super(message);
	}
}
