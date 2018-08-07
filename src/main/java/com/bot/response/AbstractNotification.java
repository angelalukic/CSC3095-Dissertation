package com.bot.response;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.channel.Channel;
import org.javacord.api.entity.message.Message;

import lombok.Getter;

@Getter
public abstract class AbstractNotification implements Notification {
	
	private Message message;
	private Map<String, Object> config;
	private List<String> flaggedWords;

	public AbstractNotification(Message message, Map<String, Object> config, List<String> flaggedWords) {
		this.message = message;
		this.config = config;
		this.flaggedWords = flaggedWords;		
	}
	
	@SuppressWarnings("unchecked")
	protected Channel retrieveReportsChannel() {
		List<Map<String, String>> yaml = (List<Map<String, String>>) config.get("channel");
		
		String reportsChannel = yaml.get(2).get("reports");
		
		if(reportsChannel != null) {
			return message.getServer().get().getChannelById(reportsChannel).get();
		}
		
		else {
			String adminChannel = yaml.get(0).get("admin");
			return message.getServer().get().getChannelById(adminChannel).get();
		}
	}
}
