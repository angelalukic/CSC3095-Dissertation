package com.bot.discord.command.commands;

import java.util.Optional;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;

import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.server.DiscordServerDTO;
import com.bot.twitter.TwitterListener;
import com.bot.twitter.TwitterServiceProxy;
import com.bot.twitter.TwitterSubscription;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Slf4j
public class TwitterAdminCommand {
	
	private Message message;
	private TwitterServiceProxy proxy;
	
	private static final String ADD_COMMAND = "\n`rf@twitter add <username>`";
	private static final String REMOVE_COMMAND = "\n`rf@twitter remove <username>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@twitter help`";
	
	public TwitterAdminCommand(Message message, TwitterServiceProxy proxy) {
		this.message = message;
		this.proxy = proxy;
	}
	
	public void execute() {
		
		try {
			Twitter twitter = TwitterFactory.getSingleton();
			String command = message.getContent().substring(11);
			
			if(command.startsWith("add")) {
				String username = message.getContent().substring(15);
				User user = twitter.showUser(username);
				executeAdd(user);
			}
			
			else if (command.startsWith("remove")) {
				String username = message.getContent().substring(18);
				User user = twitter.showUser(username);
				executeRemove(user);
			}
			
			else if (command.startsWith("help"))
				sendHelpMessage();
			
			else
				sendInvalidCommandMessage();
		}
		catch(TwitterException e) {
			log.error("Caught TwitterException: " + e.getMessage());
			sendTwitterErrorMessage();
		}
		
		// message.getContent().substring() may throw error if command is not entered correctly
		catch(IndexOutOfBoundsException e) {
			log.error("Caught IndexOutOfBoundsException: " + e.getMessage());
			sendInvalidCommandMessage();
		}
		
		// If twitter-service returns a Status 404
		catch(FeignException e) {
			log.error("Caught FeignException: " + e.getMessage());
			send404Message();
		}
		
		catch(ServerNotFoundException e) {
			log.error("Caught ServerNotFoundException: " + e.getMessage());
			sendNotCreatedMessage();
		}
	}
	
	private void executeAdd(User user) {
		if(user.getStatus() != null) 
			sendAddToTwitterService(user);
		else
			sendPrivateTwitterAccountErrorMessage(user.getScreenName());
	}
	
	private void executeRemove(User user) {
		TwitterListener listener = new TwitterListener(user.getId(), user.getScreenName());
		DiscordServerDTO server = new DiscordServerDTO(retrieveDiscordServer().getId(), retrieveDiscordServer().getName());
		TwitterSubscription subscription = new TwitterSubscription(listener, server);
		proxy.deleteSubscription(subscription);
		sendDeletedMessage(user.getScreenName());			
	}

	private void sendAddToTwitterService(User user) {
		TwitterListener listener = new TwitterListener(user.getId(), user.getScreenName());
		DiscordServerDTO server = new DiscordServerDTO(retrieveDiscordServer().getId(), retrieveDiscordServer().getName());
		TwitterSubscription subscription = new TwitterSubscription(listener, server);
		proxy.addTwitterSubscription(subscription);
		sendCreatedMessage(user.getScreenName());
	}
	
	private Server retrieveDiscordServer() {
		Optional<Server> serverOptional = message.getServer();
		
		if(serverOptional.isPresent())
			return serverOptional.get();
		else
			throw new ServerNotFoundException("id=" + message.getId());
	}
	
	private void sendSuccessMessage(String description) {
		EmbedBuilder embed = new SuccessEmbed().createEmbed(description);
		message.getChannel().sendMessage(embed);
	}
	
	private void sendErrorMessage(String description) {
		EmbedBuilder embed = new ErrorEmbed().createEmbed(description);
		message.getChannel().sendMessage(embed);
	}
	
	private void sendInvalidCommandMessage() {
		sendErrorMessage(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADD_COMMAND + REMOVE_COMMAND + HELP_COMMAND);
	}
	
	private void sendCreatedMessage(String username) {
		sendSuccessMessage(
				"Listener to Twitter account **" + username + "** has successfully been queued. A message will be sent shortly when the "
						+ "listener has been created. This may take a few minutes.\n\nMake sure to configure"
						+ " which channel future tweets are sent to using `rf@notification channel <channel>`");
	}
	
	private void sendNotCreatedMessage() {
		sendErrorMessage("**Twitter Error**: An error occured when trying to create listener.");
	}
	
	private void sendPrivateTwitterAccountErrorMessage(String username) {
		sendErrorMessage (
				"**Permission Error**: The tweets on account **" + username 
				+ "** are private and cannot be read.");
		
	}
	
	private void sendTwitterErrorMessage() {
		sendErrorMessage (
				"**Twitter Error**: An error occured when trying to create listener."
				+ " That username may not exist, or Twitter is down.");
	}
	
	private void sendDeletedMessage(String username) {
		sendSuccessMessage("Listener to Twitter account **" + username + "** successfully deleted.");
	}
	
	private void send404Message() {
		sendErrorMessage(
				"**Database Error**: An error occured when trying to update listener."
				+ " That username may not exist in our database, or the database is down.");
	}
	
	private void sendHelpMessage() {
		sendSuccessMessage (
				"These commands allow you to add and remove the Twitter channels I listen to."
				+ " When a tweet is sent out from a Twitter account I am listening to, I copy it to the channel "
				+ " specified with the `rf@notification channel <channel>` command."
				+ " Only users with the 'Administrator' permission can use these commands."
				+ " Commands are as follows:\n" 
				+ ADD_COMMAND + "\n Adds a Twitter channel for me to listen to.\n"
				+ REMOVE_COMMAND + "\n Stops me from listening to a Twitter channel.\n"
				+ "\n\nYour server will need to be registered with our database before you can use these commands."
				+ "To manually register use the following command:\n`rf@register`");
		
	}
}
