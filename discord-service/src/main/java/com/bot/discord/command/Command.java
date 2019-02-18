package com.bot.discord.command;

import org.javacord.api.entity.message.Message;

import com.bot.discord.command.commands.RoleCommand;

public class Command {
	
	private Message message;
	
	public Command(Message message) {
		this.message = message;
	}
	
	public void execute() {

		if(message.getContent().startsWith("rf!role"))
			roleCommand();
	}
	
	private void roleCommand() {
		RoleCommand command = new RoleCommand(message);
		command.execute();		
	}
}
