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
import com.bot.discord.server.DiscordServerDTO;
import com.bot.twitter.TwitterListener;
import com.bot.twitter.TwitterServiceProxy;
import com.bot.twitter.TwitterDiscordSubscription;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Slf4j
@Component
public class TwitterAdminCommand {
	
	@Autowired private TwitterServiceProxy proxy;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private User user;
	
	private static final String ADD_COMMAND = "\n`rf@twitter add <username>`";
	private static final String REMOVE_COMMAND = "\n`rf@twitter remove <username>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@twitter help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(11);																	// Can throw IndexOutOfBoundsException
			if(command.startsWith("help"))
				sendHelpCommandMessage();
			else {
				Twitter twitter = TwitterFactory.getSingleton();
				DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId()); 	// Can throw ServerNotFoundException
				if(command.startsWith("add")) {
					String username = message.getContent().substring(15);															// Can throw IndexOutOfBoundsException
					user = twitter.showUser(username);																				// Can throw TwitterException
					executeAdd(discordServer);																						// Can throw FeignException
				}
				else if (command.startsWith("remove")) {
					String username = message.getContent().substring(18);															// Can throw IndexOutOfBoundsException
					user = twitter.showUser(username);																				// Can throw TwitterExceptiton
					executeRemove(discordServer);																					// Can throw FeignException
				}
				else
					sendInvalidCommandErrorMessage();
			}
		}
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandErrorMessage();
		}
		catch(TwitterException e) {
			sendTwitterErrorMessage();
		}
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();	
		}
		catch(FeignException e) {
			send404ErrorMessage();
		}
	}
	
	private void executeAdd(DiscordServer discordServer) {
		if(user.getStatus() != null) 
			sendListenertoTwitterService(discordServer);
		else
			sendPrivateTwitterAccountErrorMessage(user.getScreenName());
	}
	
	private void executeRemove(DiscordServer discordServer) {
		TwitterListener listener = new TwitterListener(user.getId(), user.getScreenName());
		DiscordServerDTO serverDTO = new DiscordServerDTO(discordServer);
		TwitterDiscordSubscription subscription = new TwitterDiscordSubscription(listener, serverDTO);
		proxy.deleteSubscription(subscription);
		sendDeletedMessage(user.getScreenName());			
	}

	private void sendListenertoTwitterService(DiscordServer discordServer) {
		TwitterListener listener = new TwitterListener(user.getId(), user.getScreenName());
		DiscordServerDTO serverDTO = new DiscordServerDTO(discordServer);
		TwitterDiscordSubscription subscription = new TwitterDiscordSubscription(listener, serverDTO);
		proxy.addTwitterSubscription(subscription);
		sendCreatedMessage(user.getScreenName());
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Twitter Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADD_COMMAND + REMOVE_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendTwitterErrorMessage() {
		log.error("[" + server.getName() + "] Twitter returned error when trying to create listener.");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Twitter Error**: An error occured when trying to create listener."
				+ " That username may not exist, or Twitter is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Twitter Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void send404ErrorMessage() {
		log.error("[" + server.getName() + "] twitter-service returned error when trying to modify listener for account: " + user.getName());
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Database Error**: An error occured when trying to update listener."
				+ " That username may not exist in our database (if trying to execute delete), or the database is down.");
		utils.sendMessage(embed, event);
	}
	
	private void sendPrivateTwitterAccountErrorMessage(String username) {
		log.error("[" + server.getName() + "] Twitter found private account when trying to create listener for account: " + user.getName());
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Permission Error**: The tweets on account **" + username 
				+ "** are private and cannot be read.");
		utils.sendMessage(embed, event);
		
	}
	
	private void sendCreatedMessage(String username) {
		log.info("[" + server.getName() + "] Successfully created listener for account: " + user.getName());
		EmbedBuilder embed = successEmbed.createEmbed(
				"Listener to Twitter account **" + username + "** has successfully been added.\n\nMake sure to configure"
				+ " which channel future tweets are sent to using `rf@notification channel <channel>`");
		utils.sendMessage(embed, event);
	}
	
	private void sendDeletedMessage(String username) {
		log.info("[" + server.getName() + "] Successfully deleted listener for account: " + user.getName());
		EmbedBuilder embed = successEmbed.createEmbed("Listener to Twitter account **" + username + "** successfully deleted.");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Twitter Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
				"These commands allow you to add and remove the Twitter channels I listen to."
				+ " When a tweet is sent out from a Twitter account I am listening to, I copy it to the channel "
				+ " specified with the `rf@notification channel <channel>` command."
				+ " Only users with the 'Administrator' permission can use these commands."
				+ " Commands are as follows:\n" 
				+ ADD_COMMAND + "\n Adds a Twitter channel for me to listen to.\n"
				+ REMOVE_COMMAND + "\n Stops me from listening to a Twitter channel.\n"
				+ "\nYour server will need to be registered with our database before you can use these commands."
				+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}
}
