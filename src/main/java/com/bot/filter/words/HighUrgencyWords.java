package com.bot.filter.words;

import java.util.List;
import java.util.Map;

public class HighUrgencyWords extends AbstractWords {
	
	public HighUrgencyWords(List<Map<String, List<String>>> words) {
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
		
		setWhitelist(words);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(0).get("high urgency");
	}
}
