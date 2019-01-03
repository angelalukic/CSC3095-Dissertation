package com.bot.status;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import twitter4j.Status;
import twitter4j.TwitterException;

@RestController
public class StatusController {
	
	@Autowired
	private StatusDAO service;
	
	@GetMapping("/twitter/status/{id}")
	public StatusBean statusBean(@PathVariable long id) throws TwitterException {
		return new StatusBean(service.getStatus(id));
	}
	
	@PostMapping("twitter/status")
	public ResponseEntity<Object> createStatus(@RequestBody StatusBean status) throws TwitterException {
		Status savedStatus = service.postStatus(status.getBody());
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedStatus.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}
