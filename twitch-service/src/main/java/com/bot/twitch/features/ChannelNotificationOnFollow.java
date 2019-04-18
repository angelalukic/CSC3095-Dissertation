package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.FollowEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelNotificationOnFollow {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(FollowEvent.class).subscribe(event -> onFollow(event));
    }
	
    public void onFollow(FollowEvent event) {
    	log.info("[" + event.getChannel().getName() + "] FollowEvent Detected");
        String message = String.format(
                "%s has just followed!",
                event.getUser().getName()
        );
        
        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
    }
}
