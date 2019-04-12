package com.bot.discord.embed;

import java.net.URI;
import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.bot.twitch.TwitchEmbedDAO;
import com.bot.twitch.events.TwitchStreamHost;
import com.bot.twitch.events.TwitchChatMessage;
import com.bot.twitter.TwitterEmbedDAO;
import com.bot.twitter.TwitterListener;
import com.bot.twitter.TwitterStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class DiscordEmbedController {
	
	@Autowired private TwitterEmbedDAO twitterService;
	@Autowired private TwitchEmbedDAO twitchService;
	
	private static final String ID = "/{id}";
	
	@PostMapping("twitter/embed/status/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitterStatus status, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitter/embed/status/{server}");
		Message savedEmbed = twitterService.postEmbed(status, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(ID)
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("twitter/embed/listener/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitterListener listener, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitter/embed/listener/{server}");
		Message savedEmbed = twitterService.postEmbed(listener, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(ID)
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchStreamHost event, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/host/{server}");
		Message savedEmbed = twitchService.postEmbed(event, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(ID)
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("twitch/embed/chat/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchChatMessage message, @PathVariable long server) throws InterruptedException, ExecutionException {
		log.info("localhost:8080/twitch/embed/chat/{server}");
		Message savedEmbed = twitchService.postEmbed(message, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path(ID)
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}		
}
