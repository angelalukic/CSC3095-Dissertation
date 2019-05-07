package com.bot.twitch;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.discord.DiscordUtils;
import com.bot.twitch.beans.events.TwitchChatMessage;
import com.bot.twitch.beans.events.TwitchStreamHost;
import com.bot.twitch.beans.events.TwitchStreamLive;
import com.bot.twitch.beans.events.TwitchStreamOffline;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TwitchEmbedController {
	
	@Autowired private TwitchEmbedDAO service;
	@Autowired private DiscordUtils utils;
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchStreamHost event, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/host/{server}");
		Message savedEmbed = service.postEmbed(event, server);
		return utils.getResponseEntity(savedEmbed);
	}

	@PostMapping("twitch/embed/stream/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchStreamLive event, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/stream/{server}");
		Message savedEmbed = service.postEmbed(event, server);
		return utils.getResponseEntity(savedEmbed);
	}
	
	@PostMapping("twitch/embed/offline/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchStreamOffline event, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/offline/{server}");
		Message savedEmbed = service.postEmbed(event, server);
		return utils.getResponseEntity(savedEmbed);
	}
	
	@PostMapping("twitch/embed/chat/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchChatMessage message, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/chat/{server}");
		Message savedEmbed = service.postEmbed(message, server);
		return utils.getResponseEntity(savedEmbed);
	}	
}
