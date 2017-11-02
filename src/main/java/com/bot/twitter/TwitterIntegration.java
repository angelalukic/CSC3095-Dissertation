package com.bot.twitter;

import de.btobastian.javacord.entities.message.Message;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterIntegration {
	
	Message message;
	
	public TwitterIntegration(Message message) {
		this.message = message;
	}
	
	public void test() {
		
		ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setOAuthConsumerKey("SK9sjuc9HINWNiEhnwVnzEStY")
                .setOAuthConsumerSecret("KY957k34vy6z6KvEtxvyVmvkcKl2xQTwSz1ZnIt2yNCGPdfHwE")
                .setOAuthAccessToken("780352244080336896-MmKjV98lPekthHQVtpUoFgr6gOJzUm1")
                .setOAuthAccessTokenSecret("zbxhxkKXHzR56l2WfYmzfSHNuaSH2zuPhV58DHvEwNHOQ");
        
        TwitterStream twitterStream = new TwitterStreamFactory(configurationBuilder.build()).getInstance();
        
        StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
            	message.getChannelReceiver().getServer().getChannelById("375672676859248660").sendMessage(status.getText());
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
        };
        
        twitterStream.addListener(listener);
        
        FilterQuery filterQuery = new FilterQuery();
        
        filterQuery.follow(new long[]{780352244080336896L});
        
        twitterStream.filter(filterQuery);
		
	}

}
