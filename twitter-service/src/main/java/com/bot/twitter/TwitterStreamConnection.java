package com.bot.twitter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.subscription.Subscription;
import com.bot.subscription.SubscriptionDAO;
import com.bot.twitter.beans.TwitterStatus;

import lombok.extern.slf4j.Slf4j;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Slf4j
@Component
public class TwitterStreamConnection {
	
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private SubscriptionDAO subscriptionDAO;
	private TwitterStream stream;
	
	public void connectToStream() {
		stream = new TwitterStreamFactory().getInstance();
		StatusListener listener = retrieveStatusListener();
		stream.addListener(listener);
	}
	
	public void addToFilter(long[] ids) {
		FilterQuery filter = new FilterQuery(ids);
		stream.filter(filter);
	}
	
	public void addToFilter(long id) {
		List<Subscription> subscriptions = subscriptionDAO.getAllSubscriptions();
		int newSize = subscriptions.size() + 1;
		long[] ids = new long[newSize];
		for(int i = 0; i < subscriptions.size(); i++) 
			ids[i] = subscriptions.get(i).getListener().getId();
		ids[newSize-1] = id;
		addToFilter(ids);
	}
	
	private void postToDiscord(Status status) {
		TwitterStatus twitterStatus = new TwitterStatus(status);
		long userId = twitterStatus.getUser().getId();	
		List<DiscordServer> servers = subscriptionDAO.retrieveServersFromListener(userId);
		
		if(!servers.isEmpty() && !twitterStatus.isRetweet()) {
			for(int i = 0; i < servers.size(); i++) {
				log.info("Posting Tweet from " + status.getUser().getName() + " to " + servers.get(i).getName());
				proxy.sendToDiscord(twitterStatus, servers.get(i).getId());
			}
		}
	}
	
	private StatusListener retrieveStatusListener() {
		return new StatusListener() {

			@Override
			public void onException(Exception ex) {
				log.error("Error returned from StatusListener(): " + ex.getMessage());
			}

			@Override
			public void onStatus(Status status) {
				log.info("Status detected from Twitter account " + status.getUser().getName());
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
