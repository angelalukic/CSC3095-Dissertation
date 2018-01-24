package com.bot.twitch;

import java.util.TimerTask;

import com.bot.twitch.response.OfflineTwitchNotification;
import com.bot.twitch.response.OnlineTwitchNotification;
import com.bot.twitch.response.TwitchNotification;

import de.btobastian.javacord.entities.message.Message;
import lombok.extern.slf4j.Slf4j;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.endpoints.StreamEndpoint;
import me.philippheuer.twitch4j.model.Channel;

@Slf4j
public class StreamChecker extends TimerTask {
	
	private Message message;
	private TwitchClient client;
	private String channelName;
	private boolean wasOnline;
	
	public StreamChecker(Message message, TwitchClient client, String channelName) {
		this.message = message;
		this.client = client;
		this.channelName = channelName;
	}
	
	@Override
	public void run() {
		Channel channel = client.getChannelEndpoint(channelName).getChannel();
		StreamEndpoint stream = client.getStreamEndpoint();
		TwitchNotification notification;
			
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
	}
}
