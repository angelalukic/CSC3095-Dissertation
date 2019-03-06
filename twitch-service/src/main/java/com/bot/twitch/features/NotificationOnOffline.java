package com.bot.twitch.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;

public class NotificationOnOffline {
	
	public NotificationOnOffline(EventManager eventManager) {
		eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(event -> onGoOffline(event));
	}
	
	public void onGoOffline(ChannelGoOfflineEvent event) {
		// TODO
		System.out.println("NCLGamingSociety has gone offline");
	}

}
