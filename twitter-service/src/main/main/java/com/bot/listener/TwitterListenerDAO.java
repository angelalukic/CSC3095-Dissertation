package com.bot.listener;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;

public class TwitterListenerDAO {
	
	private long id;
	
	public TwitterListenerDAO(long id) {
		this.id = id;
	}
	
	public void listen() {
		
		TwitterStream stream = new TwitterStreamFactory().getInstance();
		StatusListener listener = getStatusListener();
		FilterQuery filter = new FilterQuery().follow(id);
        
        stream.filter(filter);
		stream.addListener(listener);
	}	
	
	private StatusListener getStatusListener() {
		
		return new StatusListener() {

			public void onException(Exception e) {
				// TODO Auto-generated method stub
			}

			public void onStatus(Status status) {
				// TODO Auto-generated method stub
			}

			// We do not need to do anything if the listener detects these events occurring
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { /* Do Nothing */ }
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) { /* Do Nothing */ }
			public void onScrubGeo(long userId, long upToStatusId) { /* Do Nothing */ }
			public void onStallWarning(StallWarning warning) { /* Do Nothing */ }
			
		};
	}
}
