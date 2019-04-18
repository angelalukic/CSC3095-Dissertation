package com.bot.discord.events;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.embed.template.ErrorEmbed;
import com.bot.discord.command.AdminCommand;
import com.bot.discord.command.Command;

@Component
public class MessageCreateListener {
	
	@Autowired Command command;
	@Autowired AdminCommand adminCommand;
	@Autowired ErrorEmbed errorEmbed;
	@Autowired DiscordUtils utils;
	private MessageCreateEvent event;

	public void execute(MessageCreateEvent event) {
		this.event= event;
		if (event.getMessageContent().startsWith("rf!"))
		    command.execute(event);
		else if (event.getMessageContent().startsWith("rf@")) {
			if(event.getMessage().getAuthor().isServerAdmin())
				adminCommand.execute(event);
			else
				sendInvalidPermissionsErrorMessage();
		}
	}
	
	private void sendInvalidPermissionsErrorMessage() {
		EmbedBuilder embed = errorEmbed.createEmbed("**Permission Error**: You need to have Administrative permissions to use this command.");
		utils.sendMessage(embed, event);
	}
}
