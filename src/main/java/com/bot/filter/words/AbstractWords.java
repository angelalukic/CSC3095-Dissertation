package com.bot.filter.words;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bot.filter.Filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractWords implements Words {
	
	private List<String> words;
	private List<String> exceptions;
	private Set<String> flaggedWords = new HashSet<String>();
	
	public String checkWords(String input) {
		
		flaggedWords.clear();
		
		if(countFlaggedWords(input) > countExceptions(input)) {
			return String.join(", ", flaggedWords);
		}
		return null;
	}
	
	public int countFlaggedWords(String input) {

		int count = 0;
		
		for(String word : getWords()) {
			count += countOccurences(input, word);
		}
		return count;		
	}
	
	public int countExceptions(String input) {
		
		int count = 0;
		
		for(String exception : getExceptions()) {
			count += countOccurences(input, exception);
		}
		return count;	
	}
	
	private int countOccurences(String input, String word) {
		
		int count = 0;
		int lastIndex = 0;
		String noDupes = new Filter().removeDuplicateChars(word);
			
		while(lastIndex != -1) {
			lastIndex = input.indexOf(noDupes,lastIndex);
			if(lastIndex != -1) {
				if(getWords().contains(word)) {
					flaggedWords.add(word);
				}
				count++;
				lastIndex += noDupes.length();
			}
		}
		return count;
	}
}
