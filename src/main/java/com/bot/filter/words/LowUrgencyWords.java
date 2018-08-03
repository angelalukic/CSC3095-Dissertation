package com.bot.filter.words;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

public class LowUrgencyWords extends AbstractWords {
	
	public LowUrgencyWords(Message message, List<Map<String, List<String>>> words) {
		super(message, words);
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(2).get("low urgency");
	}

	public void sendNotification() {		
	}
}
