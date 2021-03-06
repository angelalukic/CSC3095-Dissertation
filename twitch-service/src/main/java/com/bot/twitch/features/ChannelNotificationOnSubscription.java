package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.SubscriptionEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelNotificationOnSubscription {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(SubscriptionEvent.class).subscribe(event -> onSubscription(event));
    }
	
	 public void onSubscription(SubscriptionEvent event) {
		 log.info("[" + event.getChannel().getName() + "] SubscriptionEvent Detected");
	    String message = "";
	    // New Subscription
	    if (event.getMonths() <= 1) 
	        message = String.format("[Automated Message] @%s has just subscribed! Thank you so much!", event.getUser().getName());
	    // Recurring Subscription
	    if (event.getMonths() > 1) {
	        message = String.format(
	                "[Automated Message] @%s has just resubscribed! They have been subscribed for %s months! Thank you so much!",
	                event.getUser().getName(), event.getMonths()
	        );
	    }  
	    event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
	}
}
