package com.bot.discord.command;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.embed.template.ErrorEmbed;
import com.bot.discord.beans.embed.template.SuccessEmbed;
import com.bot.discord.command.commands.admin.FilterAdminCommand;
import com.bot.discord.command.commands.admin.NotificationAdminCommand;
import com.bot.discord.command.commands.admin.RegisterAdminCommand;
import com.bot.discord.command.commands.admin.RoleAdminCommand;
import com.bot.discord.command.commands.admin.TwitchAdminCommand;
import com.bot.discord.command.commands.admin.TwitterAdminCommand;
import com.bot.discord.command.commands.admin.WelcomeAdminCommand;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminCommand {

	@Autowired private RegisterAdminCommand registerAdminCommand;
	@Autowired private TwitterAdminCommand twitterAdminCommand;
	@Autowired private TwitchAdminCommand twitchAdminCommand;
	@Autowired private NotificationAdminCommand notificationAdminCommand;
	@Autowired private RoleAdminCommand roleAdminCommand;
	@Autowired private WelcomeAdminCommand welcomeAdminCommand;
	@Autowired private FilterAdminCommand filterAdminCommand;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	
	private static final String REGISTER_COMMAND = "\n`rf@register`";
	private static final String TWITTER_COMMAND = "\n`rf@twitter help`";
	private static final String NOTIFICATION_COMMAND = "\n`rf@tnotification help`";
	private static final String ROLE_COMMAND = "\n`rf@role help`";
	private static final String WELCOME_COMMAND = "\n`rf@welcome help`";
	private static final String FILTER_COMMAND = "\n`rf@filter help`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		Message message = event.getMessage();
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		if(message.getContent().startsWith("rf@register"))
			executeRegisterCommand();
		else if(message.getContent().startsWith("rf@twitter"))
			executeTwitterCommand();
		else if(message.getContent().startsWith("rf@twitch"))
			executeTwitchCommand();
		else if(message.getContent().startsWith("rf@notification"))
			executeNotificationCommand();
		else if(message.getContent().startsWith("rf@role"))
			executeRoleCommand();
		else if(message.getContent().startsWith("rf@welcome"))
			executeWelcomeCommand();
		else if(message.getContent().startsWith("rf@filter"))
			executeFilterCommand();
		else if (message.getContent().startsWith("rf@help"))
			executeHelpCommand();
		else
			sendInvalidCommandErrorMessage();
	}
	
	private void executeRegisterCommand() {
		log.info("[" + server.getName() + "] Register Admin Command Detected");
		registerAdminCommand.execute(event);
	}
	
	private void executeTwitterCommand() {
		log.info("[" + server.getName() + "] Twitter Admin Command Detected");
		twitterAdminCommand.execute(event);
	}
	
	private void executeTwitchCommand() {
		log.info("[" + server.getName() + "] Twitch Admin Command Detected");
		twitchAdminCommand.execute(event);
	}
	
	private void executeNotificationCommand() {
		log.info("[" + server.getName() + "] Notification Admin Command Detected");
		notificationAdminCommand.execute(event);
	}
	
	private void executeRoleCommand() {
		log.info("[" + server.getName() + "] Role Admin Command Detected");
		roleAdminCommand.execute(event);
	}
	
	private void executeWelcomeCommand() {
		log.info("[" + server.getName() + "] Welcome Admin Command Detected");
		welcomeAdminCommand.execute(event);
	}
	
	private void executeFilterCommand() {
		log.info("[" + server.getName() + "] Filter Admin Command Detected");
		filterAdminCommand.execute(event);
	}
	
	private void executeHelpCommand() {
		log.info("[" + server.getName() + "] Sending Role Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
			"These are the commands to help you configure your Discord server."
			+ " Only users with the 'Administrator' permission can use these commands."
			+ " Commands are as follows:\n" 
			+ REGISTER_COMMAND + "\n Register your Discord server in our database so we can store the settings for your server.\n"
			+ TWITTER_COMMAND + "\n Configure Twitter accounts. When a status is detected from these Twitter accounts, your Discord server will be notified.\n" 
			+ NOTIFICATION_COMMAND + "\n Change the channels where different notifications get sent to within your Discord server.\n" 
			+ ROLE_COMMAND + "\n Configure which roles users can assign to themselves.\n"
			+ FILTER_COMMAND + "\n Configure the word filter on a Discord Server or Twitch Channel."
			+ WELCOME_COMMAND + "\n Change the welcome message that gets sent to users when they join your Discord server.");
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ REGISTER_COMMAND + TWITTER_COMMAND + NOTIFICATION_COMMAND + ROLE_COMMAND + WELCOME_COMMAND + FILTER_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}	
}
