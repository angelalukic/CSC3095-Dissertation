package com.bot.subscription.discord;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.bot.discord.server.DiscordServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordSubscriptionDAO {
	
	public ResponseEntity<Object> deleteSubscription(DiscordSubscription subscription) {
		// TODO Finish this later
		return ResponseEntity.ok().build();
	}
	
	public DiscordServer addSubscription(DiscordSubscription subscription) {
		// TODO Finish this later
		return subscription.getServer();
	}
}
