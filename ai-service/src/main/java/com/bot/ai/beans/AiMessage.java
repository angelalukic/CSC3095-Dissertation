package com.bot.ai.beans;

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
	
	public AiMessage(AiMessage message) {
		this.messageId = message.getMessageId();
		this.authorId = message.getAuthorId();
		this.content = message.getContent();
	}
}
