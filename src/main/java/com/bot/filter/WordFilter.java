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
		
		Words words = new HighUrgencyWords(yaml);
		List<String> flaggedHighUrgencyWords = words.retrieveFlaggedWords(message);
		
		if(flaggedHighUrgencyWords != null) {
			message.getChannel().sendMessage("You said a VERY BAD word!!");
		}
	}
	
	private void checkMediumUrgencyWords(List<Map<String, List<String>>> yaml) {
		
		Words words = new MediumUrgencyWords(yaml);
		List<String> flaggedMediumUrgencyWords = words.retrieveFlaggedWords(message);
		
		if(flaggedMediumUrgencyWords != null) {
			message.getChannel().sendMessage("You said a somewhat bad word!!");
		}
	}
	
	private void checkLowUrgencyWords(List<Map<String, List<String>>> yaml) {
		
		Words words = new LowUrgencyWords(yaml);
		List<String> flaggedLowUrgencyWords = words.retrieveFlaggedWords(message);
	
		if(flaggedLowUrgencyWords != null) {
			message.getChannel().sendMessage("You said a word which is barely bad!!");
		}
	}
}
