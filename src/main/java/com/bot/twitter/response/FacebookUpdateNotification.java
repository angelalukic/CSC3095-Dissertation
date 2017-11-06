package com.bot.twitter.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import twitter4j.Status;

public class FacebookUpdateNotification extends AbstractUpdateNotification {

	public FacebookUpdateNotification(Message message, Status status) {
		super(message, status);
	}
	
	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getFacebookBlue());
		output.setAuthor("Facebook: Newcastle University Gaming Society",
				"https://www.facebook.com/groups/159328470823126/", 
				status.getUser().getProfileImageURL());
		output.setDescription(status.getText().replace("New Facebook Post from ", ""));
		
		return output;
	}
}
