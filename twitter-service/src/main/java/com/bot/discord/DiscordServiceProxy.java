package com.bot.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.twitter.beans.TwitterStatus;

@FeignClient(name="discord-service", url="localhost:8080")
public interface DiscordServiceProxy {
	
	@PostMapping("twitter/embed/status/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitterStatus status, @PathVariable("server") long server);
}
