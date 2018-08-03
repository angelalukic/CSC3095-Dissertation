package com.bot.command;

import org.javacord.api.entity.message.Message;

import lombok.Getter;

public abstract class AbstractCommand implements Command {
	
	@Getter private Message message;
	
	public AbstractCommand(Message message) {
		this.message = message;
	}
	
	// Regex will replace the first instance of rf! or rf@ with nothing - ""
	protected String retrieveCommand() {
		
		String messageContent = message.getContent();
		return messageContent.replace("rf@", ""); 
	}
}
