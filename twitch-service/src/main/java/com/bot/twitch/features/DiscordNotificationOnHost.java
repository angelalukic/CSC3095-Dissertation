package com.bot.twitch.features;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bot.Configuration;
import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.server.DiscordServer;
import com.bot.twitch.TwitchUtils;
import com.bot.twitch.features.beans.TwitchStreamHost;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.HostOnEvent;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class DiscordNotificationOnHost {
	
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private TwitchUtils utils;
	@Autowired private Configuration configuration;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(HostOnEvent.class).subscribe(event -> onHost(event, client));
	}
	
	private void onHost(HostOnEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Started Hosting: " + event.getTargetChannel().getName());
		TwitchStreamHost host = getTwitchStreamHost(event, client);
		long id = host.getHostChannel().getId();
		List<DiscordServer> servers = utils.retrieveDiscordServersForTwitchListener(id);
		if(!servers.isEmpty())
			sendToDiscordServers(host, servers);
	}
	
	private TwitchStreamHost getTwitchStreamHost(HostOnEvent event, TwitchClient client) {
		User hostChannel = utils.getUserFromHelix(event.getChannel().getName(), client);
		User targetChannel = utils.getUserFromHelix(event.getTargetChannel().getName(), client);
		Stream stream = utils.getStreamFromHelix(targetChannel.getId(), client, configuration.getIrc());
		Game game = utils.getGameFromHelix(stream.getGameId().toString(), client, configuration.getIrc());
		return new TwitchStreamHost(hostChannel, targetChannel, stream, game);
	}

	private void sendToDiscordServers(TwitchStreamHost host, List<DiscordServer> servers) {
		for(int i = 0; i < servers.size(); i++)
			proxy.sendToDiscord(host, servers.get(i).getId());
	}
}