package com.bot.discord.command;

import org.javacord.api.entity.message.Message;

import com.bot.discord.command.commands.NotificationAdminCommand;
import com.bot.discord.command.commands.RegisterAdminCommand;
import com.bot.discord.command.commands.TwitterAdminCommand;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitter.TwitterServiceProxy;

public class AdminCommand {

	private Message message;
	private DiscordServerRepository repository;
	private TwitterServiceProxy proxy;
	
	public AdminCommand(Message message, DiscordServerRepository repository, TwitterServiceProxy proxy) {
		this.message = message;
		this.repository = repository;
		this.proxy = proxy;
	}
	
	public void execute() {
		if(message.getContent().startsWith("rf@register"))
			registerCommand();
		
		else if(message.getContent().startsWith("rf@twitter"))
			twitterCommand();
		
		else if (message.getContent().startsWith("rf@notification"))
			notificationCommand();
	}
	
	private void registerCommand() {
		RegisterAdminCommand registerCommand = new RegisterAdminCommand(message, repository);
		registerCommand.execute();
	}
	
	private void twitterCommand() {
		TwitterAdminCommand twitterCommand = new TwitterAdminCommand(message, proxy);
		twitterCommand.execute();
	}
	
	private void notificationCommand() {
		NotificationAdminCommand notificationCommand = new NotificationAdminCommand(message, repository);
		notificationCommand.execute();
	}
}
