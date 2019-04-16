package com.bot.discord.command.commands.admin;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WelcomeAdminCommand {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	
	private static final String SET_COMMAND = "\n`rf@welcome set <message>`";
	private static final String CLEAR_COMMAND = "\n`rf@welcome clear`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@welcome help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(11);																	// Can throw IndexOutOfBoundsException
			if(command.startsWith("help"))
				sendHelpCommandMessage();
			else {
				DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId()); 	// Can throw ServerNotFoundException
				if(command.startsWith("set")) {
					String welcomeMessage = message.getContent().substring(15);														// Can throw IndexOutOfBoundsException
					updateWelcomeMessage(discordServer, welcomeMessage);
				}
				else if(command.startsWith("clear"))
					clearWelcomeMessage(discordServer);
				else
					sendInvalidCommandErrorMessage();
			}
		}
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandErrorMessage();
		}
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();					
		}
	}
	
	private void updateWelcomeMessage(DiscordServer discordServer, String welcomeMessage) {
		discordServer.setServerJoinMessage(welcomeMessage);
		repository.save(discordServer);
		sendSuccessfullyUpdatedWelcomeMessage();
		
	}
	
	private void clearWelcomeMessage(DiscordServer discordServer) {
		discordServer.setServerJoinMessage("");
		repository.save(discordServer);
		sendSuccessfullyClearedWelcomeMessage();
	}
	
	private void sendSuccessfullyUpdatedWelcomeMessage() {
		log.info("[" + server.getName() + "] Welcome Message Successfully Updated");
		EmbedBuilder embed = successEmbed.createEmbed(
				"Welcome message has been successfully updated. Be sure to use command `rf@notification welcome <channel>`"
				+ " to configure the channel these welcome messages get sent to.");
		utils.sendMessage(embed, event);
	}
	
	private void sendSuccessfullyClearedWelcomeMessage() {
		log.info("[" + server.getName() + "] Welcome Message Successfully Cleared");
		EmbedBuilder embed = successEmbed.createEmbed(
				"Welcome message has been successfully removed. Users will no longer receive a message upon joining this Discord server.");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Welcome Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
			"These commands allow you to change the welcome message that is sent to users who join the Discord server."
			+ " Only users with the 'Administrator' permission can use these commands."
			+ " Commands are as follows:\n" 
			+ SET_COMMAND + "\n Sets the welcome message that will be sent to users. "
			+ "If you want to have the server name in your welcome message, add `[SERVER]` into the message."
			+ "If you want to have the name of the user in your welcome message, add `[USER]` into the message."
			+ "If you want to have the server owner in your welcome message, add `[SERVEROWNER]` into the message.\n"
			+ CLEAR_COMMAND + "\n Removes the welcome message." 
			+ "\n\nYour server will need to be registered with our database before you can use these commands."
			+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Welcome Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ SET_COMMAND + CLEAR_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Welcome Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}	
}
