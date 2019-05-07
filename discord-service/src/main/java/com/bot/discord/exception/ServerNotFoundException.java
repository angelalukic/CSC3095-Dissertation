package com.bot.discord.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ServerNotFoundException extends RuntimeException {

	public ServerNotFoundException(String message) {
		super(message);
	}
}
