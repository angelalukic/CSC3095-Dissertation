package com.bot.subscription;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitter.TwitterStreamConnection;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class SubscriptionDAO {
	
	@Autowired private DiscordServerRepository discordRepository;
	@Autowired private TwitterListenerRepository twitterRepository;
	@Autowired private TwitterStreamConnection connection;
	
	public ResponseEntity<Object> deleteSubscription(Subscription subscription) {
		TwitterListener listener = getListenerInRepository(subscription.getListener().getId());
		
		if(listener == null)
			return ResponseEntity.notFound().build();
		
		boolean serverDeleted = deleteServerFromTwitterListener(listener, subscription);
		
		if(!serverDeleted)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok().build();
	}
	
	private TwitterListener getListenerInRepository(long id) {
		Optional<TwitterListener> listener = twitterRepository.findById(id);
		if(listener.isPresent())
			return listener.get();
		log.error("Listener with ID " + id + " doesn't exist in repository.");
		return null;
	}
	
	private boolean deleteServerFromTwitterListener(TwitterListener listener, Subscription subscription) {
		List<DiscordServer> servers = new ArrayList<>(listener.getServers());
		for(int i = 0; i < servers.size(); i++) {
			DiscordServer server = servers.get(i);
			if(server.getId() == subscription.getServer().getId()) {
				servers.remove(i);
				Set<DiscordServer> updatedServers = new HashSet<>(servers);
				listener.setServers(updatedServers);
				twitterRepository.save(listener);
				log.info("Server " + server.getName() + " has successfully unsubscribed from " + listener.getName());
				return true;
			}
		}
		log.error("Server " + subscription.getServer().getName() + " is not subscribed to " + listener.getName());
		return false;
	}
	
	public TwitterListener addSubscription(Subscription subscription) {
		TwitterListener listener = subscription.getListener();
		DiscordServer server = subscription.getServer();
		
		Optional<TwitterListener> optionalListener = twitterRepository.findById(listener.getId());
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		
		if(!optionalListener.isPresent()) {
			twitterRepository.save(listener);
			log.info("Listener " + listener.getName() + " did not exist in repository. New Entry Created.");
			connection.addToFilter(listener.getId());
		}
		
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return addServerToListener(listener, server);
	}
	
	private TwitterListener addServerToListener(TwitterListener listener, DiscordServer server) {
		TwitterListener repositoryListener = twitterRepository.getOne(listener.getId());
		Set<DiscordServer> servers = repositoryListener.getServers();
		if(servers == null)
			servers = new HashSet<>();
		servers.add(server);
		repositoryListener.setServers(servers);
		TwitterListener savedListener = twitterRepository.save(repositoryListener);
		log.info("Twitter Listener database entry updated for: " + listener.getName());
		return savedListener;
	}
	
	public List<DiscordServer> retrieveServersFromListener(long id) {
		TwitterListener listener = getListenerInRepository(id);
		if(listener == null)
			return Collections.emptyList();
		return new ArrayList<>(listener.getServers());
	}
	
	public List<Subscription> getAllSubscriptions() {
		List<TwitterListener> listeners = twitterRepository.findAll();
		List<Subscription> subscriptions = new ArrayList<>();
		for(int i = 0; i < listeners.size(); i++) {
			TwitterListener listener = listeners.get(i);
			Set<DiscordServer> servers = listener.getServers();
			if(servers!= null) {
				List<DiscordServer> discordServers = new ArrayList<>(servers);
				for(int j = 0; j < discordServers.size(); j++) 
					subscriptions.add(new Subscription(discordServers.get(j), listener));
			}
		}
		return subscriptions;
	}
}
