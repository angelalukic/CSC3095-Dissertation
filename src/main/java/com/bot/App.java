package com.bot;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import com.bot.yaml.YamlReader;

public class App {
	
    public static void main( String[] args ) {
    	
        try {
			System.out.println( retrieveDiscordToken() );
		} catch (FileNotFoundException e) {
			e.printStackTrace();
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
