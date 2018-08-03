package com.bot.filter.words;

import java.util.List;
import java.util.Map;

public class WhitelistedWords extends AbstractWords{
	
	public List<String> retrieveWords(List<Map<String, List<String>>> words) {
		return words.get(3).get("whitelist");
	}

}
