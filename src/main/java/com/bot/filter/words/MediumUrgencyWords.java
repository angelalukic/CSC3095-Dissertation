package com.bot.filter.words;

import java.util.List;
import java.util.Map;

public class MediumUrgencyWords extends AbstractWords {
	
	public MediumUrgencyWords(List<Map<String, List<String>>> words) {
		List<String> blacklist = retrieveBlacklist(words);
		setBlacklist(blacklist);
		
		setWhitelist(words);
	}
	
	private List<String> retrieveBlacklist(List<Map<String, List<String>>> words) {
		return words.get(1).get("medium urgency");
	}
}
