package com.bot.twitch.features;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.HostOffEvent;

public class DiscordNotificationOnUnhost {
	
	public DiscordNotificationOnUnhost(EventManager eventManager) {
		eventManager.onEvent(HostOffEvent.class).subscribe(event -> onUnhost(event));
	}
	
	public void onUnhost(HostOffEvent event) {
		//TODO
		System.out.println("NCLGamingSociety has stopped hosting");
	}
}
