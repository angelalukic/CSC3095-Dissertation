package com.bot.subscription.twitter;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bot.subscription.SubscriptionUtils;
import com.bot.twitch.listener.TwitchListener;
import com.bot.twitch.listener.TwitchListenerRepository;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TwitterSubscriptionDAO {
	
	@Autowired private SubscriptionUtils utils;
	@Autowired private TwitchListenerRepository twitchRepository;
	@Autowired private TwitterListenerRepository twitterRepository;
	
	public ResponseEntity<Object> deleteSubscription(TwitterSubscription subscription) {
		TwitchListener twitchListener = utils.getTwitchListenerInRepository(subscription.getTwitch().getId());
		if(twitchListener == null)
			return ResponseEntity.notFound().build();
		boolean twitterDeleted = utils.deleteTwitterFromTwitchListener(twitchListener, subscription);
		if(!twitterDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}
	
	public TwitchListener addSubscription(TwitterSubscription subscription) {
		TwitchListener twitchListener = subscription.getTwitch();
		TwitterListener twitterListener = subscription.getTwitter();
		
		Optional<TwitchListener> optionalListener = twitchRepository.findById(twitchListener.getId());
		Optional<TwitterListener> optionalServer = twitterRepository.findById(twitterListener.getId());
		
		if(!optionalListener.isPresent()) {
			twitchRepository.save(twitchListener);
			log.info("Twitch Listener " + twitchListener.getName() + " did not exist in repository. New Entry Created.");
			// TODO Start listening to this twitch channel
		}
		if(!optionalServer.isPresent()) {
			twitterRepository.save(twitterListener);
			log.info("TwitterListener " + twitterListener.getName() + " did not exist in repository. New Entry Created.");
		}
		return utils.addTwitterToTwitchListener(twitchListener, twitterListener);
	}
}
