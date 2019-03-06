package com.bot.twitch.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;

public class NotificationOnLive {
	
	public NotificationOnLive(EventManager eventManager) {
		eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(event -> onGoLive(event));
	}
	
	public void onGoLive(ChannelGoLiveEvent event) {
		// TODO
		System.out.println("NCLGamingSociety has gone live");
	}
}
