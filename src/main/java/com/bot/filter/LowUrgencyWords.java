package com.bot.filter;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Words which are very unlikely to cause offence
 * Administrators are notified
 */
public class LowUrgencyWords extends AbstractWords {
	
	public LowUrgencyWords() {
		addWords();
		addExceptions();
	}

	public void addWords() {
		setWords(Arrays.asList("tumblr", "4chan", "jew", "muslim", 
				"gay", "feminist", "feminism", "egalitarian", "bitch", 
				"poof", "whore", "slut", "immigrant", "midget"));
	}

	public void addExceptions() {
		// No exceptions here for now
		setExceptions(new ArrayList<String>());
	}
}
