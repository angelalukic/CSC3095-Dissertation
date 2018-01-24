package com.bot.twitch;

import com.bot.twitch.response.OfflineTwitchNotification;
import com.bot.twitch.response.OnlineTwitchNotification;
import com.bot.twitch.response.TwitchNotification;

import de.btobastian.javacord.entities.message.Message;
import lombok.extern.slf4j.Slf4j;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.endpoints.StreamEndpoint;
import me.philippheuer.twitch4j.model.Channel;

@Slf4j
public class StreamChecker {
	
	private Message message;
	private TwitchClient client;
	private String channelName;
	private boolean wasOnline;
	private final static int SLEEP_TIME = 60000;
	
	public StreamChecker(Message message, TwitchClient client, String channelName) {
		this.message = message;
		this.client = client;
		this.channelName = channelName;
	}
	
	public void start() {
		Channel channel = client.getChannelEndpoint(channelName).getChannel();
		StreamEndpoint stream = client.getStreamEndpoint();
		TwitchNotification notification;
		
		/*
		 * Infinite loop required here as we will continually check if the stream is
		 * online or offline for the duration of the bot being online
		 */
		while(true) {
			
			if(stream.isLive(channel) && !wasOnline) {
				log.info("Stream has gone online");	
				
				wasOnline = true;
				notification = new OnlineTwitchNotification(message, client);
				notification.sendNotification();

			} else if (!stream.isLive(channel) && wasOnline) {
				log.info("Stream has gone offline");
				
				wasOnline = false;
				notification = new OfflineTwitchNotification(message, client);
				notification.sendNotification();
			}
			
			try {
				Thread.sleep(SLEEP_TIME);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}
}
