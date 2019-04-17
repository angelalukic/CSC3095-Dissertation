package com.bot.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bot.discord.server.DiscordServer;
import com.bot.subscription.discord.DiscordSubscription;
import com.bot.subscription.twitter.TwitterSubscription;
import com.bot.twitch.listener.TwitchListener;
import com.bot.twitch.listener.TwitchListenerRepository;
import com.bot.twitter.listener.TwitterListener;

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
	
	public boolean deleteServerFromTwitchListener(TwitchListener listener, DiscordSubscription subscription) {
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
	
	public boolean deleteTwitterFromTwitchListener(TwitchListener twitchListener, TwitterSubscription subscription) {
		List<TwitterListener> twitterListeners = new ArrayList<>(twitchListener.getTwitterListeners());
		for(int i = 0; i < twitterListeners.size(); i++) {
			TwitterListener twitterListener = twitterListeners.get(i);
			if(twitterListener.getId() == subscription.getTwitter().getId()) {
				twitterListeners.remove(i);
				Set<TwitterListener> updatedTwitterListeners = new HashSet<>(twitterListeners);
				twitchListener.setTwitterListeners(updatedTwitterListeners);
				twitchRepository.save(twitchListener);
				log.info("Twitter " + twitterListener.getName() + " has successfully unsubscribed from " + twitchListener.getName());
				return true;
			}
		}
		log.error("Twitter " + subscription.getTwitter().getName() + " is not subscribed to " + twitchListener.getName());
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
	
	public TwitchListener addTwitterToTwitchListener(TwitchListener twitchListener, TwitterListener twitterListener) {
		TwitchListener repositoryListener = twitchRepository.getOne(twitchListener.getId());
		Set<TwitterListener> twitterListeners = repositoryListener.getTwitterListeners();
		if(twitterListeners == null)
			twitterListeners = new HashSet<>();
		twitterListeners.add(twitterListener);
		repositoryListener.setTwitterListeners(twitterListeners);
		TwitchListener savedListener = twitchRepository.save(repositoryListener);
		log.info("Twitch Listener database entry updated for: " + twitchListener.getName());
		return savedListener;
	}

}
