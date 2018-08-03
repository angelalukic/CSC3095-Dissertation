package com.bot.response;

import java.util.List;

import org.javacord.api.entity.message.Message;

import lombok.Getter;

@Getter
public abstract class AbstractNotification implements Notification {
	
	private Message message;
	private List<String> flaggedWords;
	
	public AbstractNotification(Message message, List<String> flaggedWords) {
		this.message = message;
		this.flaggedWords = flaggedWords;		
	}
}
