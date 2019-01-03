package com.bot.user;

import org.springframework.stereotype.Component;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;

@Component
public class UserDAO {
	
	public User getUser(long id) throws TwitterException {
		Twitter twitter = TwitterFactory.getSingleton();
		return twitter.showUser(id);
	}
}
