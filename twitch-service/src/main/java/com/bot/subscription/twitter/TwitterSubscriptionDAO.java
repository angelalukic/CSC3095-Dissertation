package com.bot.subscription.twitter;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bot.twitter.listener.TwitterListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TwitterSubscriptionDAO {
	
	public ResponseEntity<Object> deleteSubscription(TwitterSubscription subscription) {
		// TODO Finish this later
		return ResponseEntity.ok().build();
	}
	
	public TwitterListener addSubscription(TwitterSubscription subscription) {
		// TODO Finish this later
		return subscription.getTwitter();
	}
}
