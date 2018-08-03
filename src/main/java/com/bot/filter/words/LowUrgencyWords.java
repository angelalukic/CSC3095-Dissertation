package com.bot.filter.words;

import java.util.List;
import java.util.Map;

public class LowUrgencyWords extends AbstractWords {
	
	public LowUrgencyWords(List<Map<String, List<String>>> words) {
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
		
		setWhitelist(words);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(2).get("low urgency");
	}
}
