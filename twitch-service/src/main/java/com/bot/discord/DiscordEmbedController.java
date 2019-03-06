package com.bot.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.twitch.features.beans.TwitchStreamHost;
import com.bot.twitch.stream.TwitchStream;

@RestController
public class DiscordEmbedController {
	
	@Autowired
	private DiscordServiceProxy proxy;
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamHost event, @PathVariable("server") long server) {
		return proxy.sendToDiscord(event, server);
	}
	
	@PostMapping("twitch/embed/stream/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStream event, @PathVariable("server") long server) {
		return proxy.sendToDiscord(event, server);
	}
}
