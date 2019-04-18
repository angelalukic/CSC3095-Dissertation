package com.bot.twitch.features.discord;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.twitch.TwitchUtils;
import com.bot.twitch.beans.events.TwitchChatMessage;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.helix.domain.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class WriteChannelChatToDiscord {
	
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private TwitchUtils utils;

	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelMessageEvent.class).subscribe(event -> onChannelMessage(event, client));
	}
	
	private void onChannelMessage(ChannelMessageEvent event, TwitchClient client) {
		log.info("[" + event.getChannel().getName() + "] Message detected from: " + event.getUser().getName());
		TwitchChatMessage message = getTwitchChatMessage(event, client);
		long id = message.getChannel().getId();
		List<DiscordServer> servers = utils.retrieveDiscordServersForTwitchListener(id);
		if(!servers.isEmpty())
			sendToDiscordServers(message, servers);
	}
	
	private TwitchChatMessage getTwitchChatMessage(ChannelMessageEvent event, TwitchClient client) {
		User channel = utils.getUserFromHelix(event.getChannel().getName(), client);
		User user = utils.getUserFromHelix(event.getUser().getName(), client);
		String message = event.getMessage();
		return new TwitchChatMessage(channel, user, message);
	}
	
	private void sendToDiscordServers(TwitchChatMessage message, List<DiscordServer> servers) {
		for(int j = 0; j < servers.size(); j++)
			proxy.sendToDiscord(message, servers.get(j).getId());
	}
}
