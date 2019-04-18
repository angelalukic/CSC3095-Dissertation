package com.bot.twitch.features.discord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.twitch.TwitchUtils;
import com.bot.twitch.beans.events.TwitchStreamLive;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DiscordNotificationOnLive {
	
	@Autowired private DiscordServiceProxy discordProxy;
	@Autowired private TwitchUtils utils;
	@Autowired private Configuration configuration;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelGoLiveEvent.class).subscribe(event -> onGoLive(event, client));
	}
	
	public void onGoLive(ChannelGoLiveEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Has Started Broadcasting");
		TwitchStreamLive live = getTwitchStreamLive(event, client);
		sendToDiscordServers(live);
	}
	
	private TwitchStreamLive getTwitchStreamLive(ChannelGoLiveEvent event, TwitchClient client) {
		sleep(10); // Wait for 10 seconds before pulling information from Helix to give Twitch a chance to catch up.
		String title = event.getTitle();
		User user = utils.getUserFromHelix(event.getChannel().getName(), client);
		Stream stream = utils.getStreamFromHelix(event.getChannel().getId(), client, configuration.getIrc());
		Game game = utils.getGameFromHelix(event.getGameId().toString(), client, configuration.getIrc());
		return new TwitchStreamLive(title, user, stream, game);
	}
	
	private void sendToDiscordServers(TwitchStreamLive live) {
		long id = live.getUser().getId();
		List<DiscordServer> servers = utils.retrieveDiscordServersForTwitchListener(id);
		if(!servers.isEmpty()) {
			for(int i = 0; i < servers.size(); i++)
				discordProxy.sendToDiscord(live, servers.get(i).getId());
		}
	}
	
	private void sleep(int seconds) {
		try {
			Thread.sleep(seconds*1000L); 
		} catch (InterruptedException e) {
			log.error("Error Was Thrown when trying to Sleep on GoLiveEvent");
			Thread.currentThread().interrupt();
		}
	}
}
