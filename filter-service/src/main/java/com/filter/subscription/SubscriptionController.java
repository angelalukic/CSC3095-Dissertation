package com.filter.subscription;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.filter.message.discord.DiscordServer;
import com.filter.message.discord.DiscordServerDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class SubscriptionController {
	
	@Autowired private SubscriptionDAO service;
	
	@PostMapping("/filter/discord/blacklist/add/{word}")
	public ResponseEntity<Object> addToBlacklist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/blacklist/add/{word}");
		DiscordServer server = new DiscordServer(dto);
		DiscordServer savedServer = service.addToBlacklist(server, word);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedServer.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/filter/discord/blacklist/remove/{word}")
	public ResponseEntity<Object> removeFromBlacklist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/blacklist/remove/{word}");
		DiscordServer server = new DiscordServer(dto);
		return service.removeFromBlacklist(server, word);
	}
	
	@PostMapping("/filter/discord/greylist/add/{word}")
	public ResponseEntity<Object> addToGreylist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/greylist/add/{word}");
		DiscordServer server = new DiscordServer(dto);
		DiscordServer savedServer = service.addToGreylist(server, word);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedServer.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/filter/discord/greylist/remove/{word}")
	public ResponseEntity<Object> removeFromGreylist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/greylist/remove/{word}");
		DiscordServer server = new DiscordServer(dto);
		return service.removeFromGreylist(server, word);
	}
	
	@PostMapping("/filter/discord/whitelist/add/{word}")
	public ResponseEntity<Object> addToWhitelist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/whitelist/add/{word}");
		DiscordServer server = new DiscordServer(dto);
		DiscordServer savedServer = service.addToWhitelist(server, word);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedServer.getId()).toUri();
		return ResponseEntity.created(location).build();
	}
	
	@PostMapping("/filter/discord/whitelist/remove/{word}")
	public ResponseEntity<Object> removeFromWhitelist(@RequestBody DiscordServerDTO dto, @PathVariable String word) {
		log.info("POST localhost:8083/filter/discord/whitelist/remove/{word}");
		DiscordServer server = new DiscordServer(dto);
		return service.removeFromWhitelist(server, word);
	}
	
	@PostMapping("/filter/twitch/sync/{channel}")
	public ResponseEntity<Object> syncToTwitchChannel(@RequestBody DiscordServerDTO dto, @PathVariable long channel) {
		log.info("POST localhost:8083/filter/twitch/sync/{channel}");
		DiscordServer server = new DiscordServer(dto);
		return service.syncToTwitchChannel(server, channel);
	}
}
