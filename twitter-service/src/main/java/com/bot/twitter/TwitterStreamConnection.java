package com.bot.twitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.server.DiscordServer;
import com.bot.subscription.SubscriptionDAO;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerRepository;
import com.bot.twitter.status.TwitterStatus;

import lombok.extern.slf4j.Slf4j;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

@Slf4j
public class TwitterStreamConnection {
	
	private TwitterStream stream;
	private DiscordServiceProxy proxy;
	private SubscriptionDAO subscription;
	
	public TwitterStreamConnection(DiscordServiceProxy proxy, SubscriptionDAO subscription) {
		this.stream = new TwitterStreamFactory().getInstance();
		this.proxy = proxy;
		this.subscription = subscription;
	}
	
	public void addFilter(long[] ids) {
		TwitterStream stream = connectToStream();
		FilterQuery filter = new FilterQuery(ids);
		stream.filter(filter);
	}
	
	private TwitterStream connectToStream() {
		StatusListener listener = retrieveStatusListener();
		stream.addListener(listener);
		return stream;
	}
	
	private void postToDiscord(Status status) {
		
		TwitterStatus twitterStatus = new TwitterStatus(status);
		long userId = twitterStatus.getUser().getId();	
		List<DiscordServer> servers = subscription.retrieveServersFromListener(userId);
		
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
