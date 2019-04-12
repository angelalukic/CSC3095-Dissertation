package com.bot.discord.events;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import com.bot.discord.command.AdminCommand;
import com.bot.discord.command.Command;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitter.TwitterServiceProxy;

public class MessageCreateListener {
	
	private MessageCreateEvent event;
	private DiscordServerRepository repository;
	private TwitterServiceProxy proxy;
	
	public MessageCreateListener(MessageCreateEvent event, DiscordServerRepository repository, TwitterServiceProxy proxy) {
		this.event = event;
		this.repository = repository;
		this.proxy = proxy;
	}
	
	public void execute() {
		detectCommand();
	}
	
	private void detectCommand() {
		if (event.getMessageContent().startsWith("rf!")) {
		    Command command = new Command(event.getMessage());
		    command.execute();
		}
		else if (event.getMessageContent().startsWith("rf@")) {
			if(event.getMessage().getAuthor().isServerAdmin()) {
				AdminCommand command = new AdminCommand(event.getMessage(), repository, proxy);
				command.execute();
			}
			else
				sendErrorEmbed();
		}
	}
	
	private void sendErrorEmbed() {
		EmbedBuilder embed = new ErrorEmbed().createEmbed("**Permission Error**: You need to have Administrative permissions to use this command.");
		event.getChannel().sendMessage(embed);
	}
}
