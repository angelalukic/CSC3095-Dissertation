package com.bot.twitter.status;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import twitter4j.Status;
import twitter4j.TwitterException;

@RestController
public class TwitterStatusController {
	
	@Autowired
	private TwitterStatusDAO service;
	
	@PostMapping("twitter/status")
	public ResponseEntity<Object> createStatus(@RequestBody TwitterStatus status) throws TwitterException {
		Status savedStatus = service.postStatus(status.getBody());
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedStatus.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}