package com.bot.discord;

import java.util.Optional;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.discord.exception.ChannelNotFoundException;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

@Component
public class DiscordChannelConnection {
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private DiscordServerRepository serverRepository;
	
	
	public TextChannel connect(long server) {
		String token = "Mzc0ODUwOTAzNjQ5NDE5MjY0.DzxuUQ.53kHD4jBLFQCkqJ0T4YsnluzwAQ"; //configuration.getToken();
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		return validateDiscordChannel(server, api);
	}

	private TextChannel validateDiscordChannel(long server, DiscordApi api) {
		
		Optional<DiscordServer> optionalServer = serverRepository.findById(server);
		
		if(!optionalServer.isPresent())
			throw new ServerNotFoundException("id- " + server);
		
		DiscordServer discordServer = optionalServer.get();
		long id = discordServer.getNotificationChannel();
		
		Optional<TextChannel> optionalChannel = api.getTextChannelById(id);
		
		if(!optionalChannel.isPresent())
			throw new ChannelNotFoundException("id-" + id);
		
		return optionalChannel.get();
	}
}
