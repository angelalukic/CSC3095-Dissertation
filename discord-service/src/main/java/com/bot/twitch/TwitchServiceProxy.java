package com.bot.twitch;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.twitch.beans.TwitchSubscription;

@FeignClient(name="twitch-service", url="localhost:8082")
public interface TwitchServiceProxy {
	
	@DeleteMapping("/twitch/discord/subscription/")
	public ResponseEntity<Object> deleteDiscordSubscription(@RequestBody TwitchSubscription subscription);

	@PostMapping("/twitch/discord/subscription/")
	public ResponseEntity<Object> createDiscordSubscription(@RequestBody TwitchSubscription subscription);
}
