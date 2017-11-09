package com.bot.twitter;

import com.bot.twitter.response.FacebookUpdateNotification;
import com.bot.twitter.response.TwitterUpdateNotification;
import com.bot.twitter.response.UpdateNotification;

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

@Getter
@Setter
@Slf4j
public class TwitterIntegration {
	
	Message message;	
	
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
		if(status.getInReplyToStatusId() != status.getId()
    			|| status.isRetweet() && status.getCurrentUserRetweetId() == status.getId()) {
        	postStatus(status);
    	}
	}
	
	private void postStatus(Status status) {
		
		if(status.getText().contains("New Facebook Post from")) {
			UpdateNotification facebookUpdate = new FacebookUpdateNotification(getMessage(), status);
			facebookUpdate.sendNotification();
		} else {
			UpdateNotification twitterUpdate = new TwitterUpdateNotification(getMessage(), status);
			twitterUpdate.sendNotification();
		}
	}
}
