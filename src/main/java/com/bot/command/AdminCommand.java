package com.bot.command;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.yaml.YamlWriter;

public class AdminCommand extends AbstractCommand {
	
	public AdminCommand(Message message) {
		super(message);
	}

	public void execute() throws IOException {

		String command = retrieveCommand();
		
		if(command.startsWith("adminchannel") || command.startsWith("reportschannel")) {
			changeChannel(command);
		}
	}
	
	private void changeChannel(String command) throws IOException {
		
		try {
			String channelId = getMessage().getMentionedChannels().get(0).getIdAsString();
			
			Map<String, String> channelData = new HashMap<String, String>();
			Map<String, Object> serverData = new HashMap<String, Object>();
			
			if(command.startsWith("adminchannel")) {
				changeAdminChannel(channelId, channelData, serverData);
			}
			
			else if(command.startsWith("reportschannel")) {
				changeReportsChannel(channelId, channelData, serverData);
			}
			
		} catch (IndexOutOfBoundsException e) {
			getMessage().getChannel().sendMessage("You need to provide a valid channel.");
		}
	}

	private void changeAdminChannel(String channelId, Map<String, String> channelData, Map<String, Object> serverData) throws IOException {
		
		String adminChannelId = getMessage().getMentionedChannels().get(0).getIdAsString();
		String serverId = getMessage().getServer().get().getIdAsString();
		
		channelData.put("admin", adminChannelId);
		serverData.put("channel", channelData);
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.write(serverData); 
		
		getMessage().getChannel().sendMessage("Admin channel has been changed to <#" + adminChannelId + ">");
	}
	
	private void changeReportsChannel(String channelId, Map<String, String> channelData, Map<String, Object> serverData) throws IOException {
		
		String reportsChannelId = getMessage().getMentionedChannels().get(0).getIdAsString();
		String serverId = getMessage().getServer().get().getIdAsString();
		
		channelData.put("reports", reportsChannelId);
		serverData.put("channel", channelData);
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.write(serverData);
		
		getMessage().getChannel().sendMessage("Reports channel has been changed to <#" + reportsChannelId + ">");
	}
}
