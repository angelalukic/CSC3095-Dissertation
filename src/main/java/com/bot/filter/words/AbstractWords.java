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
public abstract class AbstractWords implements Words {
	
	private List<String> flaggedWords = new ArrayList<String>();
	@Setter private List<String> blacklist;
	private List<String> whitelist;
	private Map<String, Object> config;
	private Message message;
	
	public AbstractWords(Message message, Map<String, Object> config, List<Map<String, List<String>>> words) {
		this.whitelist = words.get(3).get("whitelist");
		this.config = config;
		this.message = message;
	}
	
	public List<String> retrieveFlaggedWords() {
		
		String messageContent = message.getContent();
		String cleanMessageContent = removeDuplicateChars(messageContent);
		String cleanerMessageContent = removeSymbols(cleanMessageContent);
		
		if(messageFlagged(cleanerMessageContent)) {
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

		Pattern pattern = Pattern.compile(word.toLowerCase());
		Matcher matcher = pattern.matcher(input.toLowerCase());
		
		while(matcher.find()) {
			flaggedWords.add(word);
			count++;
		}
		return count;
	}

	private String removeDuplicateChars(String input) {
		
		StringBuilder noDupes = new StringBuilder();
		
		for (int i = 0; i < input.length(); i++) {
	        char letter = input.charAt(i);
	        if(i == 0 || letter != input.charAt(i-1)) {
	        	noDupes.append(letter);
	        }
	    }
	    return noDupes.toString();
	}
	
	private String removeSymbols(String input) {
		return input.replaceAll("[^a-zA-Z\\s]", "");
	}
}
