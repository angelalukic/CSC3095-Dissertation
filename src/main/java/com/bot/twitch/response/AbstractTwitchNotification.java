package com.bot.twitch.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import lombok.Getter;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;

@Getter
@Setter
public abstract class AbstractTwitchNotification implements TwitchNotification {
	
	private Message message;
	private TwitchClient client;
	
	private Color twitchPurple = new Color(100,65,164);

	private String URL = "https://www.twitch.tv/nclgamingsociety";
	private String USERNAME = "nclgamingsociety";
	
	private static final String BOT_FEED_CHANNEL = "";
	
	public AbstractTwitchNotification(Message message, TwitchClient client) {
		this.message = message;
		this.client = client;
	}
	
	public void sendNotification() {
		message.getChannelReceiver().getServer().getChannelById(BOT_FEED_CHANNEL).sendMessage("", makeEmbed());
	}
	
	public String getProfileImage() {
		return client.getChannelEndpoint(USERNAME).getChannel().getLogo();
	}
	
	public String getStreamName() {
		return client.getChannelEndpoint(USERNAME).getChannel().getStatus();
	}
	
	public String getGame() {
		return client.getChannelEndpoint(USERNAME).getChannel().getGame();
	}
	
	public String getFooter() {
		return "Please note the Twitch API can sometimes be slow, so my notifications may be delayed!";
	}
}
