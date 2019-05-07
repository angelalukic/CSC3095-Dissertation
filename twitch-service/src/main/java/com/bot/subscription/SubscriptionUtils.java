package com.bot.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bot.discord.beans.server.DiscordServer;
import com.bot.twitch.beans.listener.TwitchListener;
import com.bot.twitch.beans.listener.TwitchListenerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class SubscriptionUtils {
	
	@Autowired private TwitchListenerRepository twitchRepository;
	
	public TwitchListener getTwitchListenerInRepository(long id) {
		Optional<TwitchListener> listener = twitchRepository.findById(id);
		if(listener.isPresent())
			return listener.get();
		log.error("Twitch Listener with ID " + id + " doesn't exist in repository.");
		return null;
	}
	
	public boolean deleteServerFromTwitchListener(TwitchListener listener, Subscription subscription) {
		List<DiscordServer> servers = new ArrayList<>(listener.getServers());
		for(int i = 0; i < servers.size(); i++) {
			DiscordServer server = servers.get(i);
			if(server.getId() == subscription.getServer().getId()) {
				servers.remove(i);
				Set<DiscordServer> updatedServers = new HashSet<>(servers);
				listener.setServers(updatedServers);
				twitchRepository.save(listener);
				log.info("Server " + server.getName() + " has successfully unsubscribed from " + listener.getName());
				return true;
			}
		}
		log.error("Server " + subscription.getServer().getName() + " is not subscribed to " + listener.getName());
		return false;
	}
	
	public TwitchListener addServerToTwitchListener(TwitchListener listener, DiscordServer server) {
		TwitchListener repositoryListener = twitchRepository.getOne(listener.getId());
		Set<DiscordServer> servers = repositoryListener.getServers();
		if(servers == null)
			servers = new HashSet<>();
		servers.add(server);
		repositoryListener.setServers(servers);
		TwitchListener savedListener = twitchRepository.save(repositoryListener);
		log.info("Twitch Listener database entry updated for: " + listener.getName());
		return savedListener;
	}
}
