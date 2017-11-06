package com.bot.twitter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import lombok.Getter;
import lombok.Setter;
import twitter4j.Status;

@Getter
@Setter
public abstract class AbstractUpdateNotification implements UpdateNotification {
	
	Message message;
	Status status;
	private Color twitterBlue = new Color(0,132,180);
	private Color facebookBlue = new Color(59,89,152);
	
	private static final String BOT_FEED_CHANNEL = "";
	
	public AbstractUpdateNotification(Message message, Status status) {
		this.message = message;
		this.status = status;
	}
	
	public void sendNotification() {
		getMessage().getChannelReceiver().getServer().getChannelById(BOT_FEED_CHANNEL).sendMessage("", makeEmbed());
	}
}
