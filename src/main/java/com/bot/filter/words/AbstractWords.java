package com.bot.filter.words;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bot.filter.Filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractWords implements Words {
	
	private List<String> words;
	private List<String> exceptions;
	Map<String, String> flaggedWords;
	
	public String checkWords(String[] input) {
		
		flaggedWords = findWordsToFlag(Arrays.asList(input));
		
		if(!flaggedWords.isEmpty()) {
			
			for(String word : flaggedWords.keySet()) {
				
				if(checkExceptions(word) != null) {
					return word;
				}
			}
		}
		return null;
	}
	
	public Map<String, String> findWordsToFlag(List<String> input) {
		
		flaggedWords = new HashMap<String, String>();
		
		for(String word : getWords()) {
			
			String noDupes = new Filter().removeDuplicateChars(word);
			
			for(String inputWord : input) {
				
				if(inputWord.contains(noDupes)) {
					flaggedWords.put(inputWord, word);
				}
			}
		}
		return flaggedWords;
	}

	public String checkExceptions(String flaggedWord) {
		
		for(String exception : getExceptions()) {
			
			if(flaggedWord.replaceAll("[^a-zA-Z ]", "").contains(new Filter().removeDuplicateChars(exception))) {
				return null;
			}
		}
		return flaggedWord;
	}	
}
