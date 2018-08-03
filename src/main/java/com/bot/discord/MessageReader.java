package com.bot.discord;

import java.io.IOException;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.command.AdminCommand;
import com.bot.command.Command;
import com.bot.command.UserCommand;
import com.bot.filter.WordFilter;

public class MessageReader {
	
	private Message message;
	private Map<String, Object> config;
	
	public MessageReader(Message message, Map<String, Object> config) {
		this.message = message;
		this.config = config;
	}
	
	public void read() throws IOException {
		
		WordFilter filter = new WordFilter(message, config);
		filter.checkMessage();
		
		String messageContent = message.getContent();
		
		if(messageContent.startsWith("rf!")) {
			Command command = new UserCommand(message);
			command.execute();
		} 
		
		else if (messageContent.startsWith("rf@") && message.getAuthor().isServerAdmin()) {
			Command command = new AdminCommand(message);
			command.execute();
		}
		
		else if (messageContent.startsWith("rf@") && !message.getAuthor().isServerAdmin()) {
			message.getChannel().sendMessage("Only administrators are able to use that command.");
		}
	}
}
