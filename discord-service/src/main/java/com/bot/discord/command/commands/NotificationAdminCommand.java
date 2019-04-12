package com.bot.discord.command.commands;

import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

public class NotificationAdminCommand {
	
	private Message message;
	private DiscordServerRepository repository;
	
	private static final String ADMIN_COMMAND = "\n`rf@notification admin <channel>`";
	private static final String CHANNEL_COMMAND = "\n`rf@notification channel <channel>`";
	private static final String FILTER_COMMAND = "\n`rf@notification wordfilter <channel>`";
	private static final String LOGS_COMMAND = "\n`rf@notification twitchlogs <channel>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@notification help`";
	
	public NotificationAdminCommand(Message message,  DiscordServerRepository repository) {
		this.message = message;
		this.repository = repository;
	}
	
	public void execute() {
		
		try {
			String command = message.getContent().substring(16);
			ServerTextChannel channel = message.getMentionedChannels().get(0);
			DiscordServer server = retrieveServerFromDatabase();
			
			if (command.startsWith("admin")) 
				updateAdminChannel(channel, server);
			
			else if(command.startsWith("channel")) 
				updateNotificationChannel(channel, server);
			
			else if (command.startsWith("wordfilter"))
				updateReportsChannel(channel, server);
			
			else if (command.startsWith("twitchlogs")) 
				updateLoggingChannel(channel, server);
			
			else if (command.startsWith("help"))
				sendHelpCommandMessage();
				
			else
				sendInvalidCommandMessage();
		}
		
		// message.getContent().substring() may throw error if user does not provide command
		// message.getMentionedChannels().get() may throw error if user does not provide a ServerTextChannel
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandMessage();
		}
		
		// retrieveServerFromDatabase() may throw error is server is not registered in the JPA database
		catch(ServerNotFoundException e) {
			sendErrorMessage(
					"**Server Error**: Server has not been registered, please use command `rf@register`"
					+ HELP_COMMAND);
		}
	}
	
	private void updateAdminChannel(ServerTextChannel channel, DiscordServer server) {
		long oldId = server.getAdminChannel();
		server.setAdminChannel(channel.getId());
		repository.save(server);
		sendSuccessMessage(oldId, channel, "Admin");
	}
	
	private void updateNotificationChannel(ServerTextChannel channel, DiscordServer server) {
		long oldId = server.getNotificationChannel();
		server.setNotificationChannel(channel.getId());
		repository.save(server);
		sendSuccessMessage(oldId, channel, "Notification");
	}
	
	private void updateReportsChannel(ServerTextChannel channel, DiscordServer server) {
		long oldId = server.getReportChannel();
		server.setReportChannel(channel.getId());
		repository.save(server);
		sendSuccessMessage(oldId, channel, "Word Filter");
	}
	
	private void updateLoggingChannel(ServerTextChannel channel, DiscordServer server) {
		long oldId = server.getTwitchLogChannel();
		server.setTwitchLogChannel(channel.getId());
		repository.save(server);
		sendSuccessMessage(oldId, channel, "Twitch Logging");
	}
	
	private DiscordServer retrieveServerFromDatabase() {
		
		Optional<Server> serverOptional = message.getServer();
		
		if(serverOptional.isPresent()) {
			long id = serverOptional.get().getId();
			Optional<DiscordServer> discordOptional = repository.findById(id);
				
			if(discordOptional.isPresent())
				return discordOptional.get();
					
			else 
				throw new ServerNotFoundException("id=" + message.getId());
		}				
		else 
			throw new ServerNotFoundException("id=" + message.getId());
	}
	
	private void sendSuccessMessage(long oldId, ServerTextChannel channel, String channelName) {
		EmbedBuilder embed = new SuccessEmbed().createEmbed(
				channelName + " Channel has successfully been updated from <#" + oldId 
				+ "> to <#" + channel.getId() + ">");
		message.getChannel().sendMessage(embed);
	}
	
	private void sendSuccessMessage(String description) {
		EmbedBuilder embed = new SuccessEmbed().createEmbed(description);
		message.getChannel().sendMessage(embed);
	}
	
	private void sendErrorMessage(String description) {
		EmbedBuilder embed = new ErrorEmbed().createEmbed(description);
		message.getChannel().sendMessage(embed);
	}
	
	private void sendHelpCommandMessage() {
		sendSuccessMessage(
			"These commands allow you to change the channels where notifications are send to."
			+ " Only users with the 'Administrator' permission can use these commands."
			+ " Commands are as follows:\n" 
			+ ADMIN_COMMAND + "\n Changes the channel where notifications are send by default.\n"
			+ CHANNEL_COMMAND + "\n Changes the channel where Twitter and Twitch updates are posted to.\n" 
			+ FILTER_COMMAND + "\n Changes the channel where word filter violation notifications are posted to.\n" 
			+ LOGS_COMMAND + "\n Changes the channel where Twitch Chat Logs are posted to."
			+ "\n\nYour server will need to be registered with our database before you can use these commands."
			+ "To manually register use the following command:\n`rf@register`");
	}
	
	private void sendInvalidCommandMessage() {
		sendErrorMessage(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADMIN_COMMAND + CHANNEL_COMMAND + FILTER_COMMAND + LOGS_COMMAND + HELP_COMMAND);
	}
}
