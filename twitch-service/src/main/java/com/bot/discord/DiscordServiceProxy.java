package com.bot.discord;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.twitch.beans.events.TwitchChatMessage;
import com.bot.twitch.beans.events.TwitchStreamHost;
import com.bot.twitch.beans.events.TwitchStreamLive;
import com.bot.twitch.beans.events.TwitchStreamOffline;

@FeignClient(name="discord-service", url="localhost:8080")
public interface DiscordServiceProxy {
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamHost event, @PathVariable("server") long server);
	
	@PostMapping("twitch/embed/stream/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamLive event, @PathVariable("server") long server);
	
	@PostMapping("twitch/embed/offline/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchStreamOffline event, @PathVariable("server") long server);
	
	@PostMapping("twitch/embed/chat/{server}")
	public ResponseEntity<Object> sendToDiscord(@RequestBody TwitchChatMessage message, @PathVariable("server") long server);
}
