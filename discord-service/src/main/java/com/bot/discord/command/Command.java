package com.bot.discord.command;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.command.commands.RoleCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Command {
	
	@Autowired RoleCommand roleCommand;
	private MessageCreateEvent event;
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		Message message = event.getMessage();
		if(message.getContent().startsWith("rf!role"))
			executeRoleCommand();
	}
	
	private void executeRoleCommand() {
		log.info("Role Command Detected");
		roleCommand.execute(event);
	}
}
