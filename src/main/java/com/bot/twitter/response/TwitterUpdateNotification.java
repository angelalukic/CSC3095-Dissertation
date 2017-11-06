package com.bot.twitter.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import twitter4j.Status;

public class TwitterUpdateNotification extends AbstractUpdateNotification {
	

	public TwitterUpdateNotification(Message message, Status status) {
		super(message, status);
	}
	
	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getTwitterBlue());
		output.setAuthor("Twitter: @" + status.getUser().getScreenName(), 
				"https://twitter.com/NewcastleGaming/status/" + status.getId(), 
				status.getUser().getProfileImageURL());
		output.setDescription(status.getText());
		
		return output;
	}
}
