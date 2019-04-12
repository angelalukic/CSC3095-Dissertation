package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.CheerEvent;

@Component
public class ChannelNotificationOnCheer {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(CheerEvent.class).subscribe(event -> onCheer(event));
    }
	
	public void onCheer(CheerEvent event) {
        String message = String.format(
                "%s has just cheered %s bits! Thank you so much!",
                event.getUser().getName(), event.getBits()
        );
        
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }

}
