package com.bot.twitch.listener;

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
public class TwitchListenerController {
	
	@Autowired
	private TwitchListenerRepository listenerRepository;
	
	@GetMapping("/twitch/listeners")
	public List<TwitchListener> retrieveAllListeners() {
		return listenerRepository.findAll();
	}
	
	@GetMapping("/twitch/listeners/{id}")
	public TwitchListener retrieveListener(@PathVariable long id) {
		Optional<TwitchListener> listener = listenerRepository.findById(id);
		
		if(!listener.isPresent())
			throw new TwitchListenerNotFoundException("id-" + id);
		
		return listener.get();
	}
	
	@DeleteMapping("/twitch/listeners/{id}")
	public void deleteUser(@PathVariable long id) {
		Optional<TwitchListener> listener = listenerRepository.findById(id);
		
		if(!listener.isPresent())
			throw new TwitchListenerNotFoundException("id-" + id);
		
		listenerRepository.deleteById(id);
	}
	
	@PostMapping("/twitch/listeners")
	public ResponseEntity<Object> createListener(@RequestBody TwitchListenerDTO twitch) {
		TwitchListener twitchListener = new TwitchListener(twitch);
		TwitchListener savedTwitch = listenerRepository.save(twitchListener);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(savedTwitch.getId()).toUri();
		
		return ResponseEntity.created(location).build();
	}	
}
