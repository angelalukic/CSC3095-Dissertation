package com.bot.main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bot.bean.UserBean;
import com.bot.twitter.TwitterReader;

import twitter4j.TwitterException;

@RestController
public class UserController {
	
	@GetMapping("/twitter/user/{id}")
	public UserBean userBean(@PathVariable long id) throws TwitterException {
		TwitterReader reader = new TwitterReader();
		return new UserBean(reader.getUser(id));
	}
}
