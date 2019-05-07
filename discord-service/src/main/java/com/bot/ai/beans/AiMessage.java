package com.bot.ai.beans;

import org.javacord.api.entity.message.Message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiMessage {
	
	private long messageId;
	private long authorId;
	private String content;
	
	public AiMessage() {
	}
	
	public AiMessage(Message message) {
		messageId = message.getId();
		authorId = message.getAuthor().getId();
		content = message.getContent();		
	}
}
