package com.bot.filter;

import java.util.Arrays;


/*
 * Words which will almost certainly cause offence 
 * Message is immediately removed, administrators are notified and message is sent to user
 */
public class HighUrgencyWords extends AbstractWords {
	
	public HighUrgencyWords() {
		addWords();
		addExceptions();
	}
	
	public void addWords() {
		setWords(Arrays.asList("fag", "nigger", "negro", "nazi", "pedo",
				"retard", "paki", "kike", "spaz", "tranny", "shemale", 
				"she-male", "mongoloid", "rape"));
	}
	
	public void addExceptions() {
		setExceptions(Arrays.asList("cofagrigus"));
	}
}