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
import com.bot.filter.FilterServiceProxy;
import com.github.twitch4j.helix.domain.User;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FilterAdminCommand {
	
	@Autowired private FilterServiceProxy proxy;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private User user;
	
	private static final String BLACKLIST_COMMANDS = "\n`rf@filter blacklist add/remove <word>`";
	private static final String GREYLIST_COMMANDS = "\n`rf@filter greylist add/remove <word>`";
	private static final String WHITELIST_COMMANDS = "\n`rf@filter whitelist add/remove <word>`";
	private static final String SYNC_COMMAND = "\n`rf@sync <twitch>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@filter help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		this.server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(10);																	// Can throw IndexOutOfBoundsException
			if(command.startsWith("help"))
				sendHelpCommandMessage();
			else {
				DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId());	// Can throw ServerNotFoundException
				if(command.startsWith("blacklist add")) {
					String word = message.getContent().substring(24);																// Can throw IndexOutOfBoundsException
					executeAddToBlacklist(discordServer, word);																		// Can throw FeignException
				}
				else if(command.startsWith("blacklist remove")) {
					String word = message.getContent().substring(27);																// Can throw IndexOutOfBoundsException
					executeRemoveFromBlacklist(discordServer, word);																// Can throw FeignException
				}
				else if(command.startsWith("greylist add")) {
					String word = message.getContent().substring(23);																// Can throw IndexOutOfBoundsException
					executeAddToGreylist(discordServer, word);																		// Can throw FeignException
				}
				else if(command.startsWith("greylist remove")) {
					String word = message.getContent().substring(26);																// Can throw IndexOutOfBoundsException
					executeRemoveFromGreylist(discordServer, word);																	// Can throw FeignException
				}
				else if(command.startsWith("whitelist add")) {
					String word = message.getContent().substring(24);																// Can throw IndexOutOfBoundsException
					executeAddToWhitelist(discordServer, word);																		// Can throw FeignException
				}
				else if(command.startsWith("whitelist remove")) {
					String word = message.getContent().substring(27);																// Can throw IndexOutOfBoundsException
					executeRemoveFromWhitelist(discordServer, word);																// Can throw FeignException
				}
				else if(command.startsWith("sync")) {
					String username = message.getContent().substring(15);															// Can throw IndexOutOfBoundsException
					user = utils.getTwitchUserFromHelix(username);																	// Can throw NullPointerException
					executeSync(discordServer);																									// Can throw FeignException
				}
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
		catch(FeignException e) {
			send404ErrorMessage();
		}
		catch(NullPointerException e) {
			sendTwitchErrorMessage();
		}
	}
	
	private void executeAddToBlacklist(DiscordServer discordServer, String word) {
		proxy.addToBlacklist(discordServer, word);
		sendSuccessfullyCreatedMessage("Blacklist", word);
	}

	private void executeRemoveFromBlacklist(DiscordServer discordServer, String word) {
		proxy.removeFromBlacklist(discordServer, word);
		sendSuccessfullyDeletedMessage("Blacklist", word);
	}
	
	private void executeAddToGreylist(DiscordServer discordServer, String word) {
		proxy.addToGreylist(discordServer, word);
		sendSuccessfullyCreatedMessage("Greylist", word);
	}
	
	private void executeRemoveFromGreylist(DiscordServer discordServer, String word) {
		proxy.removeFromGreylist(discordServer, word);
		sendSuccessfullyDeletedMessage("Greylist", word);
	}
	
	private void executeAddToWhitelist(DiscordServer discordServer, String word) {
		proxy.addToWhitelist(discordServer, word);
		sendSuccessfullyCreatedMessage("Whitelist", word);
	}
	
	private void executeRemoveFromWhitelist(DiscordServer discordServer, String word) {
		proxy.removeFromWhitelist(discordServer, word);
		sendSuccessfullyDeletedMessage("Whitelist", word);
	}
	
	private void executeSync(DiscordServer server) {
		proxy.syncToTwitchChannel(server, user.getId());
		sendSuccessfullySyncedMessage();
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Filter Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ BLACKLIST_COMMANDS + GREYLIST_COMMANDS + WHITELIST_COMMANDS + SYNC_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Filter Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void send404ErrorMessage() {
		log.error("[" + server.getName() + "] filter-service returned error when trying to add word to filter.");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Database Error**: An error occured when trying to update word filter."
				+ " That word may not exist in our database (if trying to execute delete), or the database is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendTwitchErrorMessage() {
		log.error("[" + server.getName() + "] Twitch returned error when trying to add filter to account: " + user.getDisplayName());
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Twitch Error**: An error occured when trying to create listener."
				+ " That username may not exist, or Twitch is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendSuccessfullyCreatedMessage(String severity, String word) {
		log.info("[" + server.getName() + "] Successfully added word \"" + word + "\" to " + severity + ".");
		EmbedBuilder embed = successEmbed.createEmbed(
				"Word \"" + word + "\" has been successfully added to " + severity + "."
				+ " Make sure to configure where word violation messages are sent to using `rf@noitification wordfilter <channel>`.");
		utils.sendMessage(embed, event);
	}
	
	private void sendSuccessfullyDeletedMessage(String severity, String word) {
		log.info("[" + server.getName() + "] Successfully removed word \"" + word + "\" from " + severity + ".");
		EmbedBuilder embed = successEmbed.createEmbed("Word \"" + word + "\" has been successfully removed from " + severity + ".");
		utils.sendMessage(embed, event);
	}
	
	private void sendSuccessfullySyncedMessage() {
		log.info("[" + server.getName() + "] Successfully synced wordfilter with Twitch Account: " + user.getDisplayName());
		EmbedBuilder embed = successEmbed.createEmbed("Word Filter has been successfully synced with **" + user.getDisplayName() + "**.");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Filter Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
				"These commands allow you to add and remove words to the word filter for a Discord Server and Twitch Channel."
				+ " When a word filter violation is detected I will notify the channel"
				+ " specified with the `rf@notification wordfilter <channel>` command."
				+ " Only users with the 'Administrator' permission can use these commands."
				+ " Commands are as follows:\n" 
				+ BLACKLIST_COMMANDS + "\n Blacklisted words will be immediately deleted and the user notified.\n"
				+ GREYLIST_COMMANDS + "\n Greylisted words will notify admins. If commanded to, I will delete the message and notify the user as with blacklisted words.\n"
				+ WHITELIST_COMMANDS + "\n Whitelisted words will prevent messages which are flagged as blacklisted from immediately being deleted if a whitelisted word is detected"
						+ " in the same sentence. Admins will still be notified, and if commanded to, I will delete the message and notify the user.\n"
				+ SYNC_COMMAND + "\n Discord server will be notified when word filter violations occur in the specified Twitch channel.\n"
				+ "\nYour server will need to be registered with our database before you can use these commands."
				+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}
}
