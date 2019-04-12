package com.bot.discord.command;

import org.javacord.api.entity.message.Message;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.command.commands.admin.NotificationAdminCommand;
import com.bot.discord.command.commands.admin.RegisterAdminCommand;
import com.bot.discord.command.commands.admin.TwitterAdminCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminCommand {

	@Autowired private RegisterAdminCommand registerAdminCommand;
	@Autowired private TwitterAdminCommand twitterAdminCommand;
	@Autowired private NotificationAdminCommand notificationAdminCommand;
	private MessageCreateEvent event;
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		Message message = event.getMessage();
		if(message.getContent().startsWith("rf@register"))
			executeRegisterCommand();
		
		else if(message.getContent().startsWith("rf@twitter"))
			executeTwitterCommand();
		
		else if (message.getContent().startsWith("rf@notification"))
			executeNotificationCommand();
	}
	
	private void executeRegisterCommand() {
		log.info("Register Admin Command Detected");
		registerAdminCommand.execute(event);
	}
	
	private void executeTwitterCommand() {
		log.info("Twitter Admin Command Detected");
		twitterAdminCommand.execute(event);
	}
	
	private void executeNotificationCommand() {
		log.info("Notification Admin Command Detected");
		notificationAdminCommand.execute(event);
	}
}
