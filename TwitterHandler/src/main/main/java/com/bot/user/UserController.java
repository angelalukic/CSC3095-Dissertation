package com.bot.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.TwitterException;

@RestController
public class UserController {
	
	@Autowired
	private UserDAO service;
	
	@GetMapping("/twitter/user/{id}")
	public UserBean userBean(@PathVariable long id) throws TwitterException {
		return new UserBean(service.getUser(id));
	}
}
