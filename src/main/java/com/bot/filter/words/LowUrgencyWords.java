package com.bot.filter.words;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.response.AdminFilterNotification;
import com.bot.response.Notification;

public class LowUrgencyWords extends AbstractWords {
	
	public LowUrgencyWords(Message message, Map<String, Object> config, List<Map<String, List<String>>> words) {
		super(message, config, words);
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(2).get("low urgency");
	}

	public void sendNotification() {	
		
		Notification adminNotification = new AdminFilterNotification(getMessage(), getConfig(), getFlaggedWords());
		adminNotification.send(new Color(255,255,0));
	}
}
