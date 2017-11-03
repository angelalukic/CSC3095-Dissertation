package com.bot.twitter;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
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
	
	private static final long TWITTER_ID = /**/;
	private static final String BOT_FEED_CHANNEL = /**/;
	private static final String TWITTER_CONSUMER_KEY = /**/;
	private static final String TWITTER_CONSUMER_SECRET = /**/;
	private static final String TWITTER_ACCESS_TOKEN = /**/;
	private static final String ACCESS_TOKEN_SECRET = /**/;
	
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

	private StatusListener createListener() {
		return new StatusListener(){
            public void onStatus(Status status) {
            	message.getChannelReceiver().getServer().getChannelById(BOT_FEED_CHANNEL)
            		.sendMessage("", postStatus(status));
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
	
	private EmbedBuilder postStatus(Status status) {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setTitle("Twitter: @" + status.getUser().getScreenName());
		output.setDescription(status.getText());
		output.setFooter(status.getCreatedAt().toString(), status.getUser().getProfileImageURL());
		output.setColor(new Color(0,132,180));
		
		return output;
	}
	
	private void addFilterQuery(TwitterStream twitterStream) {
		FilterQuery filterQuery = new FilterQuery();
        filterQuery.follow(TWITTER_ID);
        twitterStream.filter(filterQuery);
	}
}
