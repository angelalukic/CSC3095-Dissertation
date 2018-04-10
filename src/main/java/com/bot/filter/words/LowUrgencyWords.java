package com.bot.filter.words;

import java.util.Arrays;

import com.bot.filter.response.AdminFilterNotification;
import com.bot.filter.response.FilterNotification;

import de.btobastian.javacord.entities.message.Message;

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
				"poof", "whore", "slut", "immigrant", "midget", "cunt",
				"dick", "asshole"));
	}

	public void addExceptions() {
		setExceptions(Arrays.asList("jewel", "spoof", "dickens", "scunthorpe"));
	}

	public void sendFlaggedWordNotification(Message message, String word) {
		FilterNotification adminNotif = new AdminFilterNotification(message, word);
		adminNotif.sendNotification();
	}
}
