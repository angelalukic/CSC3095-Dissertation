package com.bot.discord.command.commands.admin;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.embed.template.ErrorEmbed;
import com.bot.discord.beans.embed.template.SuccessEmbed;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.twitch.TwitchServiceProxy;
import com.bot.twitch.beans.TwitchListener;
import com.bot.twitch.beans.TwitchSubscription;
import com.github.twitch4j.helix.domain.User;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TwitchAdminCommand {
	
	@Autowired private TwitchServiceProxy proxy;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private User user;
	
	private static final String ADD_COMMAND = "\n`rf@twitch add <username>`";
	private static final String REMOVE_COMMAND = "\n`rf@twitch remove <username>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@twitch help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(10);																	// Can throw IndexOutOfBoundsException
			if(command.startsWith("help"))
				sendHelpCommandMessage();
			else {
				
				DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId());	// Can throw ServerNotFoundException
				if(command.startsWith("add")) {
					String username = message.getContent().substring(14);															// Can throw IndexOutOfBoundsException
					user = utils.getTwitchUserFromHelix(username);																	// Can throw NullPointerException
					executeAdd(discordServer);																						// Can throw FeignException
				}
				else if(command.startsWith("remove")) {
					String username = message.getContent().substring(17);															// Can throw IndexOutOfBoundsException
					user = utils.getTwitchUserFromHelix(username);																	// Can throw NullPointerException
					executeDelete(discordServer);																					// Can throw FeignException
				}
				else
					sendInvalidCommandErrorMessage();
			}
		}
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandErrorMessage();
		}
		catch(NullPointerException e) {
			sendTwitchErrorMessage();
		}
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();	
		}
		catch(FeignException e) {
			send404ErrorMessage();
		}
	}
	
	private void executeAdd(DiscordServer discordServer) {
		TwitchListener listener = new TwitchListener(user.getId(), user.getDisplayName());
		TwitchSubscription subscription = new TwitchSubscription(listener, discordServer);
		proxy.createDiscordSubscription(subscription);
		sendCreatedMessage(user.getDisplayName());
	}
	
	private void executeDelete(DiscordServer discordServer) {
		TwitchListener listener = new TwitchListener(user.getId(), user.getDisplayName());
		TwitchSubscription subscription = new TwitchSubscription(listener, discordServer);
		proxy.deleteDiscordSubscription(subscription);
		sendDeletedMessage(user.getDisplayName());
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Twitch Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADD_COMMAND + REMOVE_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendTwitchErrorMessage() {
		log.error("[" + server.getName() + "] Twitch returned error when trying to create listener for account: " + user.getDisplayName());
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Twitch Error**: An error occured when trying to create listener."
				+ " That username may not exist, or Twitch is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Twitch Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void send404ErrorMessage() {
		log.error("[" + server.getName() + "] twitch-service returned error when trying to modify listener for account: " + user.getDisplayName());
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Database Error**: An error occured when trying to update listener."
				+ " That username may not exist in our database (if trying to execute delete), or the database is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendCreatedMessage(String username) {
		log.info("[" + server.getName() + "] Successfully created listener for account: " + username);
		EmbedBuilder embed = successEmbed.createEmbed(
				"Listener to Twitch account **" + username + "** has successfully been added.\n\nMake sure to configure"
				+ " which channel stream updates are sent to using `rf@notification channel <channel>`, and make sure to"
				+ " configure where Twitch logs are sent to using `rf@notification twitchlogs <channel>`.");
		utils.sendMessage(embed, event);
	}
	
	private void sendDeletedMessage(String username) {
		log.info("[" + server.getName() + "] Successfully deleted listener for account: " + username);
		EmbedBuilder embed = successEmbed.createEmbed("Listener to Twitch account **" + username + "** successfully deleted.");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Twitter Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
				"These commands allow you to add and remove the Twitch channels I listen to."
				+ " When your channel goes live or hosts another channel, I will notify the channel"
				+ " specified with the `rf@notification channel <channel>` command."
				+ " I will also copy the chatroom for your channel to the channel specified with the"
				+ " `rf@notification twitchlogs <channel>` command."
				+ " Only users with the 'Administrator' permission can use these commands."
				+ " Commands are as follows:\n" 
				+ ADD_COMMAND + "\n Adds a Twitch channel for me to listen to.\n"
				+ REMOVE_COMMAND + "\n Stops me from listening to a Twitch channel.\n"
				+ "\nYour server will need to be registered with our database before you can use these commands."
				+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}

}
