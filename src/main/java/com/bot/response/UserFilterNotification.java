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
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription("You have been caught saying " + getFlaggedWords().toString()
				+ " in server\"" + getMessage().getServer().get().getName() + "\".");
		output.addField("Original Message", getMessage().getContent());
		
		getMessage().getAuthor().asUser().get().sendMessage(output);
	}
}
