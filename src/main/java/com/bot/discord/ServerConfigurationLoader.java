package com.bot.discord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.yaml.YamlReader;

public class ServerConfigurationLoader {
	
	private Message message;
	
	public ServerConfigurationLoader(Message message) {
		this.message = message;
	}
	
	public Map<String, Object> retrieveConfiguration() throws IOException {
		
		String serverId = message.getServer().get().getIdAsString();
		
		if(!configExists(serverId)) {
			createConfigFile(serverId);
			
			String introduction = introductoryMessage();
			message.getChannel().sendMessage(introduction);
		}
		
    	YamlReader reader = new YamlReader("servers\\" + serverId + ".yml");
    	Map<String, Object> values = reader.retrieveYaml();
    	
    	return values;
	}
	
	private boolean configExists(String serverId) {
		
		try {
			@SuppressWarnings({ "unused", "resource" })
			FileInputStream stream = new FileInputStream("servers\\" + serverId + ".yml");
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	private void createConfigFile(String serverId) throws IOException {
		
		File src = new File("servers\\template.yml");
		File dest = new File("servers\\" + serverId + ".yml");
		
		Files.copy(src.toPath(), dest.toPath());
	}
	
	private String introductoryMessage() {
		return "New server detected! Please specify channel to send important notifications to using `rf@adminchannel <channel>`";
	}
}
