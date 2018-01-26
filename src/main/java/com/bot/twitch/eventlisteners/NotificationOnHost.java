package com.bot.twitch.eventlisteners;

import com.bot.twitch.response.HostTwitchNotification;
import com.bot.twitch.response.TwitchNotification;

import de.btobastian.javacord.entities.message.Message;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.channel.HostOnEvent;

public class NotificationOnHost {
	
	private Message message;
	private TwitchClient client;
	
	public NotificationOnHost(Message message, TwitchClient client) {
		this.message = message;
		this.client = client;
	}

	@EventSubscriber
    public void onHost(HostOnEvent event) {
		TwitchNotification notification = new HostTwitchNotification(message, client, event);
		notification.sendNotification();
	}
}
