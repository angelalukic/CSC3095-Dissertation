package com.bot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;

import com.bot.discord.MessageReader;
import com.bot.discord.ServerConfigurationLoader;
import com.bot.yaml.YamlReader;

import lombok.extern.java.Log;

@Log
public class App {
	
    public static void main( String[] args ) {
    	
		try {
			String token = retrieveDiscordToken();
	    	DiscordApi api = new DiscordApiBuilder().setToken(token).login().join();
	    	
	        api.addMessageCreateListener(event -> {
	        	
	        	Message message = event.getMessage();
	        	
	        	// If message is from the bot, do not do anything
	        	if(!message.getAuthor().isYourself()) {
		        	ServerConfigurationLoader configLoader = new ServerConfigurationLoader(message);
		        	
		        	try {
						Map<String, Object> config = configLoader.retrieveConfiguration();		
		        		MessageReader reader = new MessageReader(message, config);
		        		reader.read();
					} catch (IOException e) {
						log.info("An error was caught when trying to retrieve configuration file:" + e.getMessage());
					}
	        	}
	        });
	        
		} catch (FileNotFoundException e) {
			log.info("An error was caught when trying to retrieve properties from properties.yml" + e.getMessage());
		} 
    }
    
    @SuppressWarnings("unchecked")
	private static String retrieveDiscordToken() throws FileNotFoundException {
    	
    	YamlReader reader = new YamlReader("properties.yml");
    	Map<String, Object> values = reader.retrieveYaml();
    	
		List<Map<String, String>> discordAuthCodes = (List<Map<String, String>>) values.get("discord");
		
    	return discordAuthCodes.get(0).get("token");
    }
}
