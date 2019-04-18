package com.bot.discord;

import java.util.Optional;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.discord.beans.server.DiscordServerRepository;
import com.bot.discord.exception.ChannelNotFoundException;
import com.bot.discord.exception.ServerNotFoundException;

@Component
public class DiscordChannelConnection {
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private DiscordServerRepository serverRepository;
	
	public TextChannel connect(long server, String channel) {
		String token = configuration.getToken();
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		return retrieveTextChannel(server, api, channel);
	}

	private TextChannel retrieveTextChannel(long server, DiscordApi api, String channel) {
		
		Optional<DiscordServer> optionalServer = serverRepository.findById(server);
		
		if(!optionalServer.isPresent())
			throw new ServerNotFoundException("id- " + server);
		
		DiscordServer discordServer = optionalServer.get();
		long id = retrieveDiscordChannelId(discordServer, channel);
		
		Optional<TextChannel> optionalChannel = api.getTextChannelById(id);
		
		if(!optionalChannel.isPresent())
			throw new ChannelNotFoundException("id-" + id);
		
		return optionalChannel.get();
	}
	
	private long retrieveDiscordChannelId(DiscordServer server, String channel) {
		long id = server.getAdminChannel(); // Use Admin Channel by default
		if(channel.equalsIgnoreCase("notification"))
			id = server.getNotificationChannel();
		else if(channel.equalsIgnoreCase("twitchlogs"))
			id = server.getTwitchLogChannel();	
		else if (channel.equalsIgnoreCase("wordfilter"))
			id = server.getReportChannel();
		return id;
	}
}
