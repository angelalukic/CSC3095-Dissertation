package com.bot.twitter.beans.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.twitter.TwitterServiceProxy;
import com.bot.twitter.beans.TwitterSubscription;

@RestController
public class TwitterListenerController {
	
	@Autowired
	private TwitterServiceProxy proxy;
	
	@DeleteMapping("/twitter/subscription/")
	public ResponseEntity<Object> deleteSubscription(@RequestBody TwitterSubscription subscription) {
		return proxy.deleteSubscription(subscription);
	}
	
	@PostMapping("/twitter/subscription/")
	public ResponseEntity<Object> addTwitterSubscription(@RequestBody TwitterSubscription subscription) {
		return proxy.addTwitterSubscription(subscription);
	}
}
