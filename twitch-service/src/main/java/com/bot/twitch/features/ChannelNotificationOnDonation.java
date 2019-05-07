package com.bot.twitch.features;

import org.springframework.stereotype.Component;

import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.chat.events.channel.DonationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ChannelNotificationOnDonation {
	
	public void register(EventManager eventManager) {
        eventManager.onEvent(DonationEvent.class).subscribe(event -> onDonation(event));
    }

	private void onDonation(DonationEvent event) {
		log.info("[" + event.getChannel().getName() + "] DonationEvent Detected");
	    String message = String.format(
	            "[Automated Message] @%s just donated %s using %s! Thank you so much!",
	            event.getUser().getName(),
	            event.getAmount(),
	            event.getSource()
	    );
	    event.getTwitchChat().sendMessage(event.getChannel().getName(), message);
	 }
}
