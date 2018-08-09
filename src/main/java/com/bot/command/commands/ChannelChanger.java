package com.bot.command.commands;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.yaml.YamlWriter;

public class ChannelChanger {
	
	private Message message;
	private Map<String, String> channelData;
	private Map<String, Object> serverData;
	
	public ChannelChanger(Message message, Map<String, String> channelData, Map<String, Object> serverData) {
		this.message = message;
		this.channelData = channelData;
		this.serverData = serverData;
	}
	
	public void changeAdminChannel(String channelId) throws IOException {
		
		String adminChannelId = message.getMentionedChannels().get(0).getIdAsString();
		String serverId = message.getServer().get().getIdAsString();
		
		channelData.put("admin", adminChannelId);
		serverData.put("channel", channelData);
		
		String filePath = retrieveAbsoluteFilePath("servers/" + serverId + ".yml");
		
		YamlWriter yamlWriter = new YamlWriter(filePath);
		yamlWriter.replace(serverData); 
		
		message.getChannel().sendMessage("Admin channel has been changed to <#" + adminChannelId + ">");
	}
	
	public void changeReportsChannel(String channelId) throws IOException {
		
		String reportsChannelId = message.getMentionedChannels().get(0).getIdAsString();
		String serverId = message.getServer().get().getIdAsString();
		
		channelData.put("reports", reportsChannelId);
		serverData.put("channel", channelData);
		
		String filePath = retrieveAbsoluteFilePath("servers/" + serverId + ".yml");
		
		YamlWriter yamlWriter = new YamlWriter(filePath);
		yamlWriter.replace(serverData);
		
		message.getChannel().sendMessage("Reports channel has been changed to <#" + reportsChannelId + ">");
	}
	
	private String retrieveAbsoluteFilePath(String filePath) {
		
		File homedir = new File(System.getProperty("user.dir"));
		File file = new File(homedir, filePath);
		
		String absolutePath = file.getAbsolutePath();
		
		return absolutePath;
	}
}
