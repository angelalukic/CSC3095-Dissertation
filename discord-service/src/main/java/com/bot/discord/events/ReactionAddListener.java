package com.bot.discord.events;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.embed.template.ErrorEmbed;
import com.bot.discord.beans.embed.template.FilterViolationEmbed;
import com.bot.discord.beans.embed.template.SuccessEmbed;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ReactionAddListener {
	
	@Autowired private FilterViolationEmbed violationEmbed;
	@Autowired private SuccessEmbed successEmbed;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private DiscordUtils utils;
	private DiscordApi api;
	private ReactionAddEvent event;
	private Server server;
	private Embed embed;
	
	public void execute(DiscordApi api, ReactionAddEvent event) {
		this.api = api;
		this.event = event;
		this.server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		if(!event.getUser().isYourself()) {
			Optional<Message> optionalMessage = event.getMessage();
			if(optionalMessage.isPresent() ) {
				Message message = optionalMessage.get();
				List<Embed> embeds = message.getEmbeds();
				if(message.getAuthor().isYourself() && !embeds.isEmpty()) {
					embed = embeds.get(0);
					checkIfReactionOnMessageViolationEmbed(embed);
				}
			}
		}
	}
	
	private void checkIfReactionOnMessageViolationEmbed(Embed embed) {
		Optional<String> optionalTitle = embed.getTitle();
		if(optionalTitle.isPresent()) {
			String title = optionalTitle.get();
			if(title.equals("Potential Word Filter Violation") && event.getEmoji().equalsEmoji("🚮")) {
				try {
					Message message = getMessageFromEmbed(embed);
					deleteMessageAndNotifyUser(message);
				} catch (InterruptedException | ExecutionException | NullPointerException e) {
					sendUnableToDeleteErrorMessage();
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private Message getMessageFromEmbed(Embed embed) throws InterruptedException, ExecutionException {
		List<EmbedField> fields = embed.getFields();
		String messageId = null;
		String channelId = null;
		for(int i = 0; i < fields.size(); i++) {
			if(fields.get(i).getName().equals("Message ID")) 
				messageId = fields.get(i).getValue();	
			else if(fields.get(i).getName().contentEquals("Channel")) {
				String value = fields.get(i).getValue();
				channelId = value.substring(2, value.length() - 1);
			}
		}
		Optional<TextChannel> optionalChannel = api.getTextChannelById(channelId);
		if(optionalChannel.isPresent()) {
			TextChannel channel = optionalChannel.get();
			return channel.getMessageById(messageId).get();
		}
		return null;
	}
	
	private void deleteMessageAndNotifyUser(Message message) {
		if(message.canYouDelete()) {
			sendSuccessfullyDeletedMessage();
			message.delete();
		}
		else
			sendInsignificantPermissionsErrorMessage();
		sendWordViolationMessage(message);
	}
	
	private void sendSuccessfullyDeletedMessage() {
		log.info("[" + server.getId() + "] Message Violated Confirmed. Deleting Message and Notifying User.");
		EmbedBuilder builder = successEmbed.createEmbed("Message with ID " + event.getMessageId() + " has been successfully deleted.");
		utils.sendMessage(builder, event);
	}
	
	private void sendUnableToDeleteErrorMessage() {
		log.error("[" + server.getId() + "] Could not delete message. Message or channel may have been deleted.");
		EmbedBuilder builder = errorEmbed.createEmbed("**Error**: Message could not be deleted. Message may have already been deleted, or the text channel it was in has been deleted.");
		utils.sendMessage(builder, event);
	}
	
	private void sendInsignificantPermissionsErrorMessage() {
		log.error("[" + server.getId() + "] Could not delete message. Bot has insignificant permissions to delete the message.");
		EmbedBuilder builder = errorEmbed.createEmbed("**Permission Error**: Message could not be deleted. Bot does not have permission to delete the message.");
		utils.sendMessage(builder, event);
	}
	
	private void sendWordViolationMessage(Message message) {
		User user = utils.getUserFromUserOptional(message.getAuthor().asUser(), message.getId());
		EmbedBuilder builder = violationEmbed.createUserEmbed(message);
		user.sendMessage(builder);
	}
}
