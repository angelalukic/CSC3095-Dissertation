package com.bot.subscription.discord;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.subscription.SubscriptionUtils;
import com.bot.twitch.listener.TwitchListener;
import com.bot.twitch.listener.TwitchListenerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordSubscriptionDAO {
	
	@Autowired private SubscriptionUtils utils;
	@Autowired private TwitchListenerRepository twitchRepository;
	@Autowired private DiscordServerRepository discordRepository;
	
	public ResponseEntity<Object> deleteSubscription(DiscordSubscription subscription) {
		TwitchListener listener = utils.getTwitchListenerInRepository(subscription.getListener().getId());
		if(listener == null)
			return ResponseEntity.notFound().build();
		boolean serverDeleted = utils.deleteServerFromTwitchListener(listener, subscription);
		if(!serverDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}
	
	public TwitchListener addSubscription(DiscordSubscription subscription) {
		TwitchListener listener = subscription.getListener();
		DiscordServer server = subscription.getServer();
		
		Optional<TwitchListener> optionalListener = twitchRepository.findById(listener.getId());
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		
		if(!optionalListener.isPresent()) {
			twitchRepository.save(listener);
			log.info("Twitch Listener " + listener.getName() + " did not exist in repository. New Entry Created.");
			// TODO Start listening to this twitch channel
		}
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Discord Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return utils.addServerToTwitchListener(listener, server);
	}
}
