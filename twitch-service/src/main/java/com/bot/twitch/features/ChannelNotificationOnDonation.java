package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.DonationEvent;

@Component
public class ChannelNotificationOnDonation {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(DonationEvent.class).subscribe(event -> onDonation(event));
    }

	 private void onDonation(DonationEvent event) {
	        String message = String.format(
	                "%s just donated %s using %s!",
	                event.getUser().getName(),
	                event.getAmount(),
	                event.getSource()
	        );

	        event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
	    }
}
