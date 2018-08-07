package com.bot.filter.words;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.response.AdminFilterNotification;
import com.bot.response.Notification;
import com.bot.response.UserFilterNotification;

public class MediumUrgencyWords extends AbstractWords {
	
	public MediumUrgencyWords(Message message, Map<String, Object> config, List<Map<String, List<String>>> words) {
		super(message, config, words);
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(1).get("medium urgency");
	}

	public void sendNotification() {

		Notification userNotification = new UserFilterNotification(getMessage(), getConfig(), getFlaggedWords());
		userNotification.send(new Color(255,165,0));
		
		Notification adminNotification = new AdminFilterNotification(getMessage(), getConfig(), getFlaggedWords());
		adminNotification.send(new Color(255,165,0));
	}
}
