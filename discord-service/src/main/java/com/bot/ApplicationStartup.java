package com.bot;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.discord.events.MessageCreateListener;
import com.bot.discord.events.ReactionAddListener;
import com.bot.discord.events.ServerJoinListener;
import com.bot.discord.events.ServerMemberJoinListener;

import lombok.extern.slf4j.Slf4j;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Slf4j
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private Configuration configuration;	
	@Autowired private ServerJoinListener serverJoinListener;
	@Autowired private ServerMemberJoinListener serverMemberJoinListener;
	@Autowired private MessageCreateListener messageCreateListener;
	@Autowired private ReactionAddListener reactionAddListener;

	@Override
	public void onApplicationEvent(final ApplicationReadyEvent applicationReadyEvent) {
		
		String token = configuration.getToken();
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
		// ChatBot joins a server
		api.addServerJoinListener(event -> {
			log.info("[" + event.getServer().getName() + "] ServerJoinEvent");
			serverJoinListener.execute(event);
		});	
		
		// User joins a server the chatbot is in
		api.addServerMemberJoinListener(event -> {
			log.info("[" + event.getServer().getName() + "] ServerMemberJoinEvent");
			serverMemberJoinListener.execute(event);
		});
		
		// Message is sent in the server the chatbot is in
		api.addMessageCreateListener(event -> {
			log.debug("MessageCreateEvent");
            messageCreateListener.execute(event);
        });
		
		api.addReactionAddListener(event -> {
			log.info("[" + event.getServer().get().getName() + "] ReactionAddListener");
			reactionAddListener.execute(api, event);
		});
	}
}
