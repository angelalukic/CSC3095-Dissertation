package com.bot.filter;

import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.filter.words.HighUrgencyWords;
import com.bot.filter.words.LowUrgencyWords;
import com.bot.filter.words.MediumUrgencyWords;
import com.bot.filter.words.Words;

public class WordFilter {
	
	private Message message;
	private Map<String, Object> config;
	
	public WordFilter(Message message, Map<String, Object> config) {
		this.message = message;
		this.config = config;
	}
	
	public void checkMessage() {
		
		List<Map<String, List<String>>> yaml = retrieveFilterWords(config);
		
		checkHighUrgencyWords(yaml);
		checkMediumUrgencyWords(yaml);
		checkLowUrgencyWords(yaml);
	}
	
	@SuppressWarnings("unchecked")
	private List<Map<String, List<String>>> retrieveFilterWords(Map<String, Object> config) {
		List<Map<String, List<String>>> yaml = (List<Map<String, List<String>>>) config.get("word filter");
		return yaml;
	}
	
	private void checkHighUrgencyWords(List<Map<String, List<String>>> yaml) {
		
		Words words = new HighUrgencyWords(message, config, yaml);
		List<String> flaggedHighUrgencyWords = words.retrieveFlaggedWords();
		
		if(flaggedHighUrgencyWords != null) {
			words.sendNotification();
		}
	}
	
	private void checkMediumUrgencyWords(List<Map<String, List<String>>> yaml) {
		
		Words words = new MediumUrgencyWords(message, config, yaml);
		List<String> flaggedMediumUrgencyWords = words.retrieveFlaggedWords();
		
		if(flaggedMediumUrgencyWords != null) {
			words.sendNotification();
		}
	}
	
	private void checkLowUrgencyWords(List<Map<String, List<String>>> yaml) {
		
		Words words = new LowUrgencyWords(message, config, yaml);
		List<String> flaggedLowUrgencyWords = words.retrieveFlaggedWords();
	
		if(flaggedLowUrgencyWords != null) {
			words.sendNotification();
		}
	}
}
