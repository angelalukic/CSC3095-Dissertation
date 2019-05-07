package com.bot.discord.events;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.ai.AiDAO;
import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.embed.template.ErrorEmbed;
import com.bot.discord.command.AdminCommand;
import com.bot.discord.command.Command;
import com.bot.filter.FilterDAO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageCreateListener {
	
	@Autowired Command command;
	@Autowired AdminCommand adminCommand;
	@Autowired ErrorEmbed errorEmbed;
	@Autowired DiscordUtils utils;
	@Autowired FilterDAO filter;
	@Autowired AiDAO ai;
	private MessageCreateEvent event;

	public void execute(MessageCreateEvent event) {
		this.event = event;
		if(!event.getMessageAuthor().isYourself()) {
			ai.sendMessageToAi(event);
			if (event.getMessageContent().startsWith("rf!"))
			    command.execute(event);
			else if (event.getMessageContent().startsWith("rf@")) {
				if(event.getMessage().getAuthor().isServerAdmin())
					adminCommand.execute(event);
				else
					sendInvalidPermissionsErrorMessage();
			}
			if(!event.getMessage().getAuthor().isServerAdmin() || !event.getMessageContent().startsWith("rf@filter"))
				filter.assessMessage(event);
		}	
		else
			addReactionToEmbed();
	}
	
	private void sendInvalidPermissionsErrorMessage() {
		EmbedBuilder embed = errorEmbed.createEmbed("**Permission Error**: You need to have Administrative permissions to use this command.");
		utils.sendMessage(embed, event);
	}
	
	private void addReactionToEmbed() {
		List<Embed> embeds = event.getMessage().getEmbeds();
		if(!embeds.isEmpty()) {
			Embed embed = embeds.get(0);
			Optional<String> optionalTitle = embed.getTitle();
			if(optionalTitle.isPresent()) {
				String title = optionalTitle.get();
				if(title.equals("Potential Word Filter Violation")) {
					log.info("Adding Emoji to Potential Word Filter Violation Embed");
					event.getMessage().addReaction("🚮");
				}
			}
		}
	}
}
