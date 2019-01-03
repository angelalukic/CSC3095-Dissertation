package com.bot.status;

import org.springframework.stereotype.Component;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

@Component
public class StatusDAO {
	
	public Status getStatus(long id) throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		return twitter.showStatus(id);
	}
}
