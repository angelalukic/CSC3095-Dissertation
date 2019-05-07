package com.bot.subscription;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bot.twitter.beans.listener.TwitterListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SubscriptionController {

	@Autowired private SubscriptionDAO service;
	
	@DeleteMapping("/twitter/subscription/")
	public ResponseEntity<Object> deleteSubscription(@RequestBody Subscription subscription) {
		log.info("DELETE localhost:8081/twitter/subscription/");
		return service.deleteSubscription(subscription);
	}
	
	@PostMapping("/twitter/subscription/")
	public ResponseEntity<Object> createSubscription(@RequestBody Subscription subscription) {
		log.info("POST localhost:8081/twitter/subscription");	
		TwitterListener listener = service.addSubscription(subscription);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(listener.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
}
