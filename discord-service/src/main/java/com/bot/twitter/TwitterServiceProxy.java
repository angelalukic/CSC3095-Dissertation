package com.bot.twitter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="twitter-service", url="localhost:8081")
public interface TwitterServiceProxy {
	
	@DeleteMapping("/twitter/subscription/")
	public ResponseEntity<Object> deleteSubscription(@RequestBody TwitterDiscordSubscription subscription);
	
	@PostMapping("/twitter/subscription/")
	public ResponseEntity<Object> addTwitterSubscription(@RequestBody TwitterDiscordSubscription subscription);
}
