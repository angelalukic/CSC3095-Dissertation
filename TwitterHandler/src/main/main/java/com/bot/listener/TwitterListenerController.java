package com.bot.listener;

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
public class TwitterListenerController {
	
	@Autowired
	private TwitterListenerRepository listenerRepository;
	
	@Autowired
	private DiscordServerRepository serverRepository;
	
	@GetMapping("/twitter/listeners")
	public List<TwitterListener> retrieveAllListeners() {
		return listenerRepository.findAll();
	}
	
	@GetMapping("/twitter/listeners/{id}")
	public TwitterListener retrieveListener(@PathVariable long id) {
		Optional<TwitterListener> listener = listenerRepository.findById(id);
		
		if(!listener.isPresent())
			throw new TwitterListenerNotFoundException("id-" + id);
		
		return listener.get();
	}
	
	@DeleteMapping("/twitter/listeners/{id}")
	public void deleteUser(@PathVariable long id) {
		listenerRepository.deleteById(id);
	}
	
	@PostMapping("/twitter/listeners")
	public ResponseEntity<Object> createListener(@RequestBody TwitterListener twitter) {
		TwitterListener savedTwitter = listenerRepository.save(twitter);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTwitter.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}
	
	@GetMapping("/twitter/listeners/{id}/servers")
	public List<DiscordServer> retrieveAllUsers(@PathVariable long id) {
		Optional<TwitterListener> listener = listenerRepository.findById(id);
	
		if(!listener.isPresent())
			throw new TwitterListenerNotFoundException("id-" + id);
		
		return listener.get().getServers();
	}
	
	@DeleteMapping("/twitter/listeners/{id}/servers")
	public void deleteDiscordServer(@PathVariable long id, @RequestBody DiscordServer server) {
		Optional<TwitterListener> listenerOptional = listenerRepository.findById(id);
		
		if(!listenerOptional.isPresent())
			throw new TwitterListenerNotFoundException("id-" + id);
		
		TwitterListener listener = listenerOptional.get();
		
		server.setListener(listener);
		serverRepository.delete(server);		
	}
	
	@PostMapping("/twitter/listeners/{id}/servers")
	public ResponseEntity<Object> addDiscordServer(@PathVariable long id, @RequestBody DiscordServer server) {
		Optional<TwitterListener> listenerOptional = listenerRepository.findById(id);
		
		if(!listenerOptional.isPresent())
			throw new TwitterListenerNotFoundException("id-" + id);
		
		TwitterListener listener = listenerOptional.get();
		
		server.setListener(listener);
		serverRepository.save(server);
		
		URI location = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(server.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}	
}
