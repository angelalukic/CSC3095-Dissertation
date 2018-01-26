package com.bot.twitch.eventlisteners;

import com.bot.twitch.response.TwitchNotification;
import com.bot.twitch.response.UnhostTwitchNotification;

import de.btobastian.javacord.entities.message.Message;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.EventSubscriber;
import me.philippheuer.twitch4j.events.event.channel.HostOffEvent;

public class NotificationOnUnhost {
	
	private Message message;
	private TwitchClient client;
	
	public NotificationOnUnhost(Message message, TwitchClient client) {
		this.message = message;
		this.client = client;
	}
	
	@EventSubscriber
    public void onHost(HostOffEvent event) {
		TwitchNotification notification = new UnhostTwitchNotification(message, client, event);
		notification.sendNotification();
		}
}
