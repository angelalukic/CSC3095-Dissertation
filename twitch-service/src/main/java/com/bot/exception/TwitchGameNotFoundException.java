package com.bot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class TwitchGameNotFoundException extends RuntimeException {
	
	public TwitchGameNotFoundException(String message) {
		super(message);
	}
}
