package com.bot.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import twitter4j.TwitterException;

@RestController
public class StatusController {
	
	@Autowired
	private StatusDAO service;
	
	@GetMapping("/twitter/status/{id}")
	public StatusBean statusBean(@PathVariable long id) throws TwitterException {
		return new StatusBean(service.getStatus(id));
	}
}
