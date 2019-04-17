package com.bot.subscription.twitter;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bot.twitch.listener.TwitchListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TwitterSubscriptionController {
	
	@Autowired private TwitterSubscriptionDAO service;

	
	@DeleteMapping("/twitch/twitter/subscription/")
	public ResponseEntity<Object> deleteTwitterSubscription(@RequestBody TwitterSubscription subscription) {
		log.info("DELETE localhost:8082/twitter/subscription");
		return service.deleteSubscription(subscription);
	}
	
	@PostMapping("/twitch/twitter/subscription/")
	public ResponseEntity<Object> createTwitterSubscription(@RequestBody TwitterSubscription subscription) {
		log.info("POST localhost:8082/twitter/subscription");
		TwitchListener listener = service.addSubscription(subscription);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(listener.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

}
