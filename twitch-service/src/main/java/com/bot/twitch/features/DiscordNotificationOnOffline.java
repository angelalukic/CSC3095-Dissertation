package com.bot.twitch.features;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.server.DiscordServer;
import com.bot.twitch.TwitchUtils;
import com.bot.twitch.features.beans.TwitchStreamOffline;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;
import com.github.twitch4j.helix.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordNotificationOnOffline {
	
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private TwitchUtils utils;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelGoOfflineEvent.class).subscribe(event -> onGoOffline(event, client));
	}
	
	public void onGoOffline(ChannelGoOfflineEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Has Stopped Broadcasting");
		TwitchStreamOffline offline = getTwitchStreamOffline(event, client);
		long id = offline.getUser().getId();
		List<DiscordServer> servers = utils.retrieveDiscordServersForTwitchListener(id);
		if(!servers.isEmpty())
			sendToDiscordServers(offline, servers);
		
	}
	
	private TwitchStreamOffline getTwitchStreamOffline(ChannelGoOfflineEvent event, TwitchClient client) {
		User user = utils.getUserFromHelix(event.getChannel().getName(), client);
		return new TwitchStreamOffline(user);
	}
	
	private void sendToDiscordServers(TwitchStreamOffline offline, List<DiscordServer> servers) {
		for(int i = 0; i < servers.size(); i++)
			proxy.sendToDiscord(offline, servers.get(i).getId());
	}
}
