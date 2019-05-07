package com.bot.discord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.twitch.beans.events.TwitchChatMessage;
import com.bot.twitch.beans.events.TwitchStreamHost;
import com.bot.twitch.beans.events.TwitchStreamLive;

@RestController
public class DiscordEmbedController {
	
	@Autowired
	private DiscordServiceProxy proxy;
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamHost event, @PathVariable("server") long server) {
		return proxy.sendToDiscord(event, server);
	}
	
	@PostMapping("twitch/embed/stream/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamLive event, @PathVariable("server") long server) {
		return proxy.sendToDiscord(event, server);
	}
	
	@PostMapping("twitch/embed/chat/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchChatMessage message, @PathVariable("server") long server) {
		return proxy.sendToDiscord(message, server);
	}
}
