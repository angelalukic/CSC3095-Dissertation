package com.bot.twitter;

import com.bot.twitter.response.FacebookUpdateNotification;
import com.bot.twitter.response.TwitterUpdateNotification;
import com.bot.twitter.response.UpdateNotification;
import com.bot.twitter.response.YouTubeUpdateNotification;

import de.btobastian.javacord.entities.message.Message;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

@Slf4j
public class TwitterIntegration {
	
	@Getter @Setter private Message message;	
	
	private static final long TWITTER_ID = 780352244080336896L;
	private static final String TWITTER_CONSUMER_KEY = "";
	private static final String TWITTER_CONSUMER_SECRET = "";
	private static final String TWITTER_ACCESS_TOKEN = "";
	private static final String ACCESS_TOKEN_SECRET = "";
	
	public TwitterIntegration(Message message) {
		this.message = message;
	}
	
	public void retrieveStream() {
		TwitterStream twitterStream = buildTwitterStream();
        
        StatusListener listener = createListener();
        twitterStream.addListener(listener);
        
        addFilterQuery(twitterStream);		
	}

	private TwitterStream buildTwitterStream() {
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY)
                .setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET)
                .setOAuthAccessToken(TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(ACCESS_TOKEN_SECRET);
        
		return new TwitterStreamFactory(configurationBuilder.build()).getInstance();
	}
	
	private void addFilterQuery(TwitterStream twitterStream) {
		FilterQuery filterQuery = new FilterQuery();
        filterQuery.follow(TWITTER_ID);
        twitterStream.filter(filterQuery);
	}

	private StatusListener createListener() {
		return new StatusListener(){
            public void onStatus(Status status) {
            	checkStatus(status);
            }
			
            public void onException(Exception ex) {
            	log.error(ex.getMessage());
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) { /*Do nothing*/ }
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) { /*Do nothing*/ }
			public void onScrubGeo(long arg0, long arg1) { /*Do nothing*/ }
			public void onStallWarning(StallWarning arg0) { /*Do nothing*/ }
        };
	}
	
	private void checkStatus(Status status) {
		if(!isReply(status) && !status.isRetweet()) {
        	postStatus(status);
    	}
	}

	private boolean isReply(Status status) {
		if (status.getInReplyToScreenName() != null) {
			return true;
		} else {
			return false;
		}
	}
	
	private void postStatus(Status status) {
		
		/*
		 * Twitter account automatically gets updates from the NUGS Facebook Group through zapier.com
		 * All these updates will have "New Facebook Post" appended to the front
		 */
		if(status.getText().startsWith("New Facebook Post")) {
			UpdateNotification facebookUpdate = new FacebookUpdateNotification(message, status);
			facebookUpdate.sendNotification();
		} 
		
		/*
		 * Twitter account automatically gets updates from the NUGS YouTube page through YouTube
		 * All these updates will have "via @YouTube" appended to the end
		 */
		else if(status.getText().endsWith("via @YouTube")) {
			UpdateNotification youTubeUpdate = new YouTubeUpdateNotification(message, status);
			youTubeUpdate.sendNotification();
		} else {
			UpdateNotification twitterUpdate = new TwitterUpdateNotification(message, status);
			twitterUpdate.sendNotification();
		}
	}
}
