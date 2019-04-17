package com.bot.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TwitterListenerController {
	
	@Autowired
	private TwitterServiceProxy proxy;
	
	@DeleteMapping("/twitter/subscription/")
	public ResponseEntity<Object> deleteSubscription(@RequestBody TwitterDiscordSubscription subscription) {
		return proxy.deleteSubscription(subscription);
	}
	
	@PostMapping("/twitter/subscription/")
	public ResponseEntity<Object> addTwitterSubscription(@RequestBody TwitterDiscordSubscription subscription) {
		return proxy.addTwitterSubscription(subscription);
	}
}
