package com.bot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.TwitterException;

@RestController
public class TwitterUserController {
	
	@Autowired
	private TwitterUserDAO service;
	
	@GetMapping("/twitter/users/{id}")
	public TwitterUser userBean(@PathVariable long id) throws TwitterException {
		return new TwitterUser(service.getUser(id));
	}
}
