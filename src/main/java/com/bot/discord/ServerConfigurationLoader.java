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
		
		String filePath = retrieveAbsoluteFilePath("servers/" + serverId + ".yml");
		
    	YamlReader reader = new YamlReader(filePath);
    	Map<String, Object> values = reader.retrieveYaml();
    	
    	return values;
	}
	
	private boolean configExists(String serverId) {
		
		String filePath = retrieveAbsoluteFilePath("servers/" + serverId + ".yml");
		
		try {
			@SuppressWarnings({ "unused", "resource" })
			FileInputStream stream = new FileInputStream(filePath);
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}
	
	private void createConfigFile(String serverId) throws IOException {
		
		String srcPath = retrieveAbsoluteFilePath("servers/template.yml");
		String destPath = retrieveAbsoluteFilePath("servers/" + serverId + ".yml");
		
		File src = new File(srcPath);
		File dest = new File(destPath);
		
		Files.copy(src.toPath(), dest.toPath());
	}
	
	private String retrieveAbsoluteFilePath(String filePath) {
		
		File homedir = new File(System.getProperty("user.dir"));
		File file = new File(homedir, filePath);
		
		String absolutePath = file.getAbsolutePath();
		
		return absolutePath;
	}
	
	private String introductoryMessage() {
		return "New server detected! Please specify channel to send important notifications to using `rf@adminchannel <channel>`";
	}
}
