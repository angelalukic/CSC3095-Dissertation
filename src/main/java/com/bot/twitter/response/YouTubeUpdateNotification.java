package com.bot.twitter.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import twitter4j.Status;

public class YouTubeUpdateNotification extends AbstractUpdateNotification {
	
	public YouTubeUpdateNotification(Message message, Status status) {
		super(message, status);
	}

	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getYouTubeRed());
		output.setAuthor("YouTube: @Newastle University Gaming Society", 
				"https://www.youtube.com/channel/UC7PeKnuBCx5MDPj_f1V90sQ", 
				status.getUser().getProfileImageURL());
		output.setDescription("A new YouTube video has been uploaded: " + status.getText());
		
		return output;
	}
}
