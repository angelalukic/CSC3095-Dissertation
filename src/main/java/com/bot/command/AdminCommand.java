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
		
		if(command.startsWith("adminchannel")) {
			
			changeAdminChannel();
		}
	}

	private void changeAdminChannel() throws IOException {
		String serverId = getMessage().getServer().get().getIdAsString();
		String adminChannelId = getMessage().getMentionedChannels().get(0).getIdAsString();
		
		Map<String, String> adminChannel = new HashMap<String, String>();
		Map<String, Object> data = new HashMap<String, Object>();
		
		adminChannel.put("admin", adminChannelId);
		data.put("channel", adminChannel);
		
		YamlWriter yamlwriter = new YamlWriter("servers\\" + serverId + ".yml");
		yamlwriter.write(data); 
		
		getMessage().getChannel().sendMessage("Admin channel has been changed to <#" + adminChannelId + ">");
	}
}
