package com.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.discord.events.MessageCreateListener;
import com.bot.discord.events.ServerJoinListener;
import com.bot.discord.events.ServerMemberJoinListener;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitter.TwitterServiceProxy;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private Configuration configuration;
	@Autowired private DiscordServerRepository repository;
	@Autowired private TwitterServiceProxy proxy;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
		
		String token = configuration.getToken();
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
		api.addServerJoinListener(event -> {
			ServerJoinListener serverJoinListener = new ServerJoinListener(event, repository);
			serverJoinListener.execute();
		});	
		
		api.addServerMemberJoinListener(event -> {
			ServerMemberJoinListener serverMemberJoinListener = new ServerMemberJoinListener();
			serverMemberJoinListener.execute(event);
		});
		
		api.addMessageCreateListener(event -> {
			MessageCreateListener messageCreateListener = new MessageCreateListener(event, repository, proxy);
            messageCreateListener.execute();
        });
	}
}
