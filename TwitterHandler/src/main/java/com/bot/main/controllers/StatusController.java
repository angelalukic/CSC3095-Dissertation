package com.bot.main.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.bot.bean.StatusBean;
import com.bot.twitter.TwitterReader;

import twitter4j.TwitterException;

@RestController
public class StatusController {
	
	@GetMapping("/twitter/status/{id}")
	public StatusBean statusBean(@PathVariable long id) throws TwitterException {
		TwitterReader reader = new TwitterReader();
		return new StatusBean(reader.getStatus(id));
	}
}
