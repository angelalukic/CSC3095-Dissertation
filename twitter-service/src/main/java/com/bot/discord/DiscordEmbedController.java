package com.bot.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.status.TwitterStatus;

@RestController
public class DiscordEmbedController {
	
	@Autowired
	private DiscordServiceProxy proxy;
	
	@PostMapping("/discord/embed/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitterStatus status, @PathVariable long server) {
		return proxy.sendToDiscord(status, server);
	}
}
