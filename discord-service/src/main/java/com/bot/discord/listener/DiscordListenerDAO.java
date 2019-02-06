package com.bot.discord.listener;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import com.bot.Configuration;

public class DiscordListenerDAO {
	
	@Autowired
	private Configuration configuration;
	
	public void listen() {
		
		String secret = configuration.getSecret();
		DiscordApi api = new DiscordApiBuilder().setToken(secret).login().join();

		api.addMessageCreateListener(event-> {
			// TODO
		});
	}
}
