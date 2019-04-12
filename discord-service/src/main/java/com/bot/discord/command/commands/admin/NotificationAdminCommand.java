package com.bot.discord.command.commands.admin;

import org.javacord.api.entity.channel.ServerTextChannel;
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
public class NotificationAdminCommand {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private ServerTextChannel channel;
	
	private static final String ADMIN_COMMAND = "\n`rf@notification admin <channel>`";
	private static final String CHANNEL_COMMAND = "\n`rf@notification channel <channel>`";
	private static final String FILTER_COMMAND = "\n`rf@notification wordfilter <channel>`";
	private static final String LOGS_COMMAND = "\n`rf@notification twitchlogs <channel>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@notification help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(16); 																	// Can throw IndexOutOfBoundsException
			if (command.startsWith("help"))
				sendHelpCommandMessage();
			else {
				channel = message.getMentionedChannels().get(0); 																	// Can throw IndexOutOfBoundsException
				DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId()); 	// Can throw ServerNotFoundException
				if (command.startsWith("admin")) 
					updateAdminChannel(discordServer);
				else if(command.startsWith("channel")) 
					updateNotificationChannel(discordServer);
				else if (command.startsWith("wordfilter"))
					updateReportsChannel(discordServer);
				else if (command.startsWith("twitchlogs")) 
					updateLoggingChannel(discordServer);
				else
					sendInvalidCommandErrorMessage();
			}
		}
		// message.getContent().substring() may throw error if user does not provide command
		// message.getMentionedChannels().get() may throw error if user does not provide a ServerTextChannel
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandErrorMessage();
		}
			
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();					
		}
	}
	
	private void updateAdminChannel(DiscordServer discordServer) {
		long oldId = discordServer.getAdminChannel();
		long newId = channel.getId();
		discordServer.setAdminChannel(newId);
		repository.save(discordServer);
		sendSuccessfullyChangedChannelMessage(oldId, newId, "Admin");
	}
	
	private void updateNotificationChannel(DiscordServer discordServer) {
		long oldId = discordServer.getNotificationChannel();
		long newId = channel.getId();
		discordServer.setNotificationChannel(newId);
		repository.save(discordServer);
		sendSuccessfullyChangedChannelMessage(oldId, newId, "Notification");
	}
	
	private void updateReportsChannel(DiscordServer discordServer) {
		long oldId = discordServer.getReportChannel();
		long newId = channel.getId();
		discordServer.setReportChannel(newId);
		repository.save(discordServer);
		sendSuccessfullyChangedChannelMessage(oldId, newId, "Word Filter");
	}
	
	private void updateLoggingChannel(DiscordServer discordServer) {
		long oldId = discordServer.getTwitchLogChannel();
		long newId = channel.getId();
		discordServer.setTwitchLogChannel(newId);
		repository.save(discordServer);
		sendSuccessfullyChangedChannelMessage(oldId, newId, "Twitch Logging");
	}
	
	private void sendSuccessfullyChangedChannelMessage(long oldId, long newId, String channelName) {
		log.info("[" + server.getName() + "] " + channelName + " Channel updated to " + channel.getName());
		EmbedBuilder embed = successEmbed.createEmbed(
				channelName + " Channel has successfully been updated from <#" + oldId 
				+ "> to <#" + newId + ">");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Notification Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
			"These commands allow you to change the channels where notifications are send to."
			+ " Only users with the 'Administrator' permission can use these commands."
			+ " Commands are as follows:\n" 
			+ ADMIN_COMMAND + "\n Changes the channel where notifications are send by default.\n"
			+ CHANNEL_COMMAND + "\n Changes the channel where Twitter and Twitch updates are posted to.\n" 
			+ FILTER_COMMAND + "\n Changes the channel where word filter violation notifications are posted to.\n" 
			+ LOGS_COMMAND + "\n Changes the channel where Twitch Chat Logs are posted to."
			+ "\n\nYour server will need to be registered with our database before you can use these commands."
			+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Notification Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADMIN_COMMAND + CHANNEL_COMMAND + FILTER_COMMAND + LOGS_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Notification Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
}
