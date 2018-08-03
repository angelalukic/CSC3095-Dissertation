package com.bot.filter.words;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.javacord.api.entity.message.Message;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractWords implements Words {
	
	private List<String> flaggedWords = new ArrayList<String>();
	private List<String> blacklist;
	private List<String> whitelist;
	
	protected void setWhitelist(List<Map<String, List<String>>> words) {
		this.whitelist = words.get(3).get("whitelist");
	}
	
	public List<String> retrieveFlaggedWords(Message message) {
		
		String messageContent = message.getContent();
		
		if(messageFlagged(messageContent)) {
			return flaggedWords;
		}
		return null;		
	}
	
	private boolean messageFlagged(String messageContent) {
		
		if(countBlacklistedWords(messageContent) > countWhitelistedWords(messageContent)) {
			return true;
		}
		return false;		
	}
	
	private int countBlacklistedWords(String input) {
		
		int count = 0;
		
		for(String word : getBlacklist()) {
			count += countOccurencesOfWord(input, word);
		}
		return count;	
		
	}
	
	private int countWhitelistedWords(String input) {
		
		int count = 0;
		
		for(String word : getWhitelist()) {
			count += countOccurencesOfWord(input, word);
		}
		return count;	
	}
	
	private int countOccurencesOfWord(String input, String word) {
		
		int count = 0;

		Pattern pattern = Pattern.compile(word);
		Matcher matcher = pattern.matcher(input);
		
		while(matcher.find()) {
			flaggedWords.add(word);
			count++;
		}
		return count;
	}
}
