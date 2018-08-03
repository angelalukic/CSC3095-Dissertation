package com.bot.discord;

import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.filter.WordFilter;

public class MessageReader {
	
	private Message message;
	private Map<String, Object> config;
	
	public MessageReader(Message message, Map<String, Object> config) {
		this.message = message;
		this.config = config;
	}
	
	public void read() {
		
		WordFilter filter = new WordFilter(message, config);
		filter.checkMessage();
	}
}
