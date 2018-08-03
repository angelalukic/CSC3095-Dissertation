package com.bot.command;

import org.javacord.api.entity.message.Message;

public class UserCommand extends AbstractCommand {
	
	private Message message;
	
	public UserCommand(Message message) {
		super(message);
	}

	public void execute() {
	}
}
