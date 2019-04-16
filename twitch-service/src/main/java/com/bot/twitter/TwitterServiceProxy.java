package com.bot.twitter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.twitch.features.beans.TwitchStreamLive;

@FeignClient(name="twitter-service", url="localhost:8081")
public interface TwitterServiceProxy {
	
	@PostMapping("twitter/status/{server}")
	public ResponseEntity<Object> createStatus(@RequestBody TwitchStreamLive event, @PathVariable("server") long server);
}
