package com.bot.filter.words;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.response.AdminFilterNotification;
import com.bot.response.Notification;
import com.bot.response.UserFilterNotification;

public class HighUrgencyWords extends AbstractWords {
	
	public HighUrgencyWords(Message message, Map<String, Object> config, List<Map<String, List<String>>> words) {
		super(message, config, words);
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(0).get("high urgency");
	}

	public void sendNotification() {
		
		getMessage().delete();
		
		Notification userNotification = new UserFilterNotification(getMessage(), getConfig(), getFlaggedWords());
		userNotification.send(new Color(255,0,0));
		
		Notification adminNotification = new AdminFilterNotification(getMessage(), getConfig(), getFlaggedWords());
		adminNotification.send(new Color(255,0,0));
	}
}
