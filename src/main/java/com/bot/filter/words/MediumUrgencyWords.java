package com.bot.filter.words;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * Words when misused can cause offence 
 * Administrators are notified and message is sent to user
 */
public class MediumUrgencyWords extends AbstractWords {
	
	public MediumUrgencyWords() {
		addWords();
		addExceptions();
	}

	public void addWords() {
		setWords(Arrays.asList("trigger", "sjw", "loli", "shota", "communist", 
				"communism", "ukip", "bnp", "hitler", "adolf", "terrorist", 
				"terrorism", "chink", "allahu akbar", "autistic", "autism",
				"queer", "dyke", "spastic"));
	}

	public void addExceptions() {
		// No exceptions here for now
		setExceptions(new ArrayList<String>());
	}
}
