package com.bot.twitter;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

public class TwitterReader {
	
	public User getUser(long id) throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		return twitter.showUser(id);
	}
	
	public Status getStatus(long id) throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		return twitter.showStatus(id);
	}
}
