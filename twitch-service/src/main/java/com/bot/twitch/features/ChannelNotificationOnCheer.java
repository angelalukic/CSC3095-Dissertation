package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.CheerEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelNotificationOnCheer {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(CheerEvent.class).subscribe(event -> onCheer(event));
    }
	
	public void onCheer(CheerEvent event) {
		log.info("[" + event.getChannel().getName() + "] CheerEvent Detected");
        String message = String.format(
                "[Automated Message] @%s has just cheered %s bits! Thank you so much!",
                event.getUser().getName(), event.getBits()
        );
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}
