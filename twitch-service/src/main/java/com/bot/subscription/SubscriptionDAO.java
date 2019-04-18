package com.bot.subscription;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bot.discord.beans.server.DiscordServer;
import com.bot.discord.beans.server.DiscordServerRepository;
import com.bot.twitch.TwitchChannelConnection;
import com.bot.twitch.beans.listener.TwitchListener;
import com.bot.twitch.beans.listener.TwitchListenerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SubscriptionDAO {
	
	@Autowired private SubscriptionUtils utils;
	@Autowired private TwitchListenerRepository twitchRepository;
	@Autowired private DiscordServerRepository discordRepository;
	@Autowired private TwitchChannelConnection connection;
	
	public ResponseEntity<Object> deleteSubscription(Subscription subscription) {
		TwitchListener listener = utils.getTwitchListenerInRepository(subscription.getListener().getId());
		if(listener == null)
			return ResponseEntity.notFound().build();
		boolean serverDeleted = utils.deleteServerFromTwitchListener(listener, subscription);
		if(!serverDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}
	
	public TwitchListener addSubscription(Subscription subscription) {
		TwitchListener listener = subscription.getListener();
		DiscordServer server = subscription.getServer();
		
		Optional<TwitchListener> optionalListener = twitchRepository.findById(listener.getId());
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		
		if(!optionalListener.isPresent()) {
			twitchRepository.save(listener);
			log.info("Twitch Listener " + listener.getName() + " did not exist in repository. New Entry Created.");
			connection.listenToChannel(listener.getName());
		}
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Discord Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return utils.addServerToTwitchListener(listener, server);
	}
}
