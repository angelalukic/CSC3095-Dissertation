package com.bot.discord.server;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class DiscordServerController {
	
	@Autowired
	private DiscordServerRepository serverRepository;
	
	@GetMapping("/discord/servers")
	public List<DiscordServer> retrieveAllListeners() {
		return serverRepository.findAll();
	}
	
	@GetMapping("/discord/servers/{id}")
	public DiscordServer retrieveListener(@PathVariable long id) {
		Optional<DiscordServer> server = serverRepository.findById(id);
		
		if(!server.isPresent())
			throw new DiscordServerNotFoundException("id-" + id);
		
		return server.get();
	}
	
	@DeleteMapping("/discord/servers/{id}")
	public void deleteUser(@PathVariable long id) {
		Optional<DiscordServer> server = serverRepository.findById(id);
		
		if(!server.isPresent())
			throw new DiscordServerNotFoundException("id-" + id);
		
		serverRepository.deleteById(id);
	}
	
	@PostMapping("/discord/servers")
	public ResponseEntity<Object> createListener(@RequestBody DiscordServerDTO server) {
		DiscordServer discordServer = new DiscordServer(server);
		DiscordServer savedDiscord = serverRepository.save(discordServer);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedDiscord.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}	
}
