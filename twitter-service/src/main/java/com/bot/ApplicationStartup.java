package com.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.h2.Subscription;
import com.bot.h2.SubscriptionRepository;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerRepository;
import com.bot.twitter.status.TwitterStatus;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired TwitterListenerRepository listenerRepository;
	@Autowired SubscriptionRepository subscriptionRepository;
	@Autowired DiscordServiceProxy proxy;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
	 
		List<TwitterListener> listeners = listenerRepository.findAll();
		
		for(int i = 0; i < listeners.size(); i++) {
			long id = listeners.get(i).getId();
			listen(id);
		}	
	}
	
	private void listen(long userId) {
		StatusListener listener = retrieveStatusListener();
		TwitterStream stream = new TwitterStreamFactory().getInstance();
		stream.addListener(listener);
		FilterQuery filter = new FilterQuery().follow(userId);
		stream.filter(filter);
	}
	
	private void postToDiscord(Status status) {
		
		TwitterStatus twitterStatus = new TwitterStatus(status);
		long userId = twitterStatus.getUser().getId();		
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		
		for(int i = 0; i < subscriptions.size(); i++) {
			Subscription subscription = subscriptions.get(i);
			
			if(subscription.getListener().getId() == userId && !twitterStatus.isRetweet()) {
				long serverId = subscription.getServer().getId();
				proxy.sendToDiscord(twitterStatus, serverId);
			}
		}
	}
	
	private StatusListener retrieveStatusListener() {
		
		return new StatusListener() {

			@Override
			public void onException(Exception ex) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void onStatus(Status status) {
				postToDiscord(status);
			}

			// We are content doing nothing when these statuses occur
			@Override public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { /* Do Nothing */ }
			@Override public void onTrackLimitationNotice(int numberOfLimitedStatuses) { /* Do Nothing */ }
			@Override public void onScrubGeo(long userId, long upToStatusId) { /* Do Nothing */ }
			@Override public void onStallWarning(StallWarning warning) { /* Do Nothing */	}
		};
	}
}
