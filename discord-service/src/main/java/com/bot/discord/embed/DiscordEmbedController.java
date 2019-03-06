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
import com.bot.twitter.TwitterEmbedDAO;
import com.bot.twitter.TwitterStatus;

@RestController
public class DiscordEmbedController {
	
	@Autowired private TwitterEmbedDAO twitterService;
	@Autowired private TwitchEmbedDAO twitchService;
	
	@PostMapping("twitter/embed/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitterStatus status, @PathVariable long server) throws InterruptedException, ExecutionException {
		Message savedEmbed = twitterService.postEmbed(status, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("twitch/embed/host/{server}")
	public ResponseEntity<Object> createDiscordEmbed(@RequestBody TwitchStreamHost event, @PathVariable long server) throws InterruptedException, ExecutionException {
		Message savedEmbed = twitchService.postEmbed(event, server);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedEmbed.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
}
