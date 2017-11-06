package com.bot.filter.words;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractWords implements Words {
	
	private List<String> words;
	private List<String> exceptions;
	
	public Map<String, String> checkWords(List<String> input) {
		
		Map<String, String> flaggedWords = new HashMap<String, String>();
		
		for(String word : getWords()) {
			
			for(String inputWord : input) {
				
				if(inputWord.contains(word)) {
					flaggedWords.put(inputWord, word);
				}
			}
		}
		return flaggedWords;
	}

	public String checkExceptions(String flaggedWord) {
		
		for(String exception : getExceptions()) {
			
			if(flaggedWord.replaceAll("[^a-zA-Z ]", "").contains(exception)) {
				return null;
			}
		}
		return flaggedWord;
	}	
}
