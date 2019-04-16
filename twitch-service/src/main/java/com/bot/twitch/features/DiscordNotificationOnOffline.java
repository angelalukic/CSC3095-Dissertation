package com.bot.twitch.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitch.TwitchUtils;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordNotificationOnOffline {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private TwitchUtils utils;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(event -> onGoOffline(event, client));
	}
	
	public void onGoOffline(ChannelGoOfflineEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Has Stopped Broadcasting");
	}
}
