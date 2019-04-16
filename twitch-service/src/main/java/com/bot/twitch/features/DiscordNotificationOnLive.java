package com.bot.twitch.features;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitch.TwitchUtils;
import com.bot.twitch.listener.TwitchListenerRepository;
import com.bot.twitter.TwitterServiceProxy;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordNotificationOnLive {
	
	@Autowired private DiscordServerRepository discordRepository;
	@Autowired private TwitchListenerRepository twitterRepository;
	@Autowired private DiscordServiceProxy discordProxy;
	@Autowired private TwitterServiceProxy twitterProxy;
	@Autowired private TwitchUtils utils;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(event -> onGoLive(event, client));
	}
	
	public void onGoLive(ChannelGoLiveEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Has Started Broadcasting");
	}
}
