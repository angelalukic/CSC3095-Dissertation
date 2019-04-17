package com.bot.twitch;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.twitch.subscription.TwitchDiscordSubscription;
import com.bot.twitch.subscription.TwitchTwitterSubscription;

@FeignClient(name="twitch-service", url="localhost:8082")
public interface TwitchServiceProxy {
	
	@DeleteMapping("/twitch/discord/subscription/")
	public ResponseEntity<Object> deleteDiscordSubscription(@RequestBody TwitchDiscordSubscription subscription);

	@PostMapping("/twitch/discord/subscription/")
	public ResponseEntity<Object> createDiscordSubscription(@RequestBody TwitchDiscordSubscription subscription);
		
	@DeleteMapping("/twitch/twitter/subscription/")
	public ResponseEntity<Object> deleteTwitterSubscription(@RequestBody TwitchTwitterSubscription subscription);

	@PostMapping("/twitch/twitter/subscription/")
	public ResponseEntity<Object> createTwitterSubscription(@RequestBody TwitchTwitterSubscription subscription);
}
