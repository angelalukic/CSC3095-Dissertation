package com.bot.response;

import java.awt.Color;
import java.util.List;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class UserFilterNotification extends AbstractNotification {

	public UserFilterNotification(Message message, List<String> flaggedWords) {
		super(message, flaggedWords);
	}

	public void send(Color color) {
		
		String flaggedWords = getFlaggedWords().toString();
		String serverName = getMessage().getServer().get().getName();
		String messageContent = getMessage().getContent();
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription("You have been caught saying " + flaggedWords
				+ " in server\"" + serverName + "\".");
		output.addField("Original Message", messageContent);
		
		getMessage().getAuthor().asUser().get().sendMessage(output);
	}
}
