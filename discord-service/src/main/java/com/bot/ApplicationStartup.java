package com.bot;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.discord.command.AdminCommand;
import com.bot.discord.command.Command;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired
	private Configuration configuration;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
		
		String token = configuration.getToken();
		DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
		
		api.addMessageCreateListener(event -> {
            if (event.getMessageContent().startsWith("rf!")) {
                Command command = new Command(event.getMessage());
                command.execute();
            }
            if (event.getMessageContent().startsWith("rf@")) {
            	AdminCommand command = new AdminCommand(event.getMessage());
            	command.execute();
            }
        });
	}
}
