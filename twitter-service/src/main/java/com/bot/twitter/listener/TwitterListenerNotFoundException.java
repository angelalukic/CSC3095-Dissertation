package com.bot.twitter.listener;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TwitterListenerNotFoundException extends RuntimeException {
	
	public TwitterListenerNotFoundException(String message) {
		super(message);
	}
}
