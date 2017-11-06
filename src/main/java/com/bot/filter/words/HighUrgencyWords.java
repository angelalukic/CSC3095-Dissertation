package com.bot.filter.words;

import java.util.Arrays;

import com.bot.filter.response.AdminFilterNotification;
import com.bot.filter.response.FilterNotification;
import com.bot.filter.response.UserFilterNotification;

import de.btobastian.javacord.entities.message.Message;


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
		setExceptions(Arrays.asList("cofagrigus", "scrape"));
	}

	public void sendFlaggedWordNotification(Message message, String word) {		
		FilterNotification userNotif = new UserFilterNotification(message, getFlaggedWords().get(word));
		userNotif.sendWarning();
		
		FilterNotification adminNotif = new AdminFilterNotification(message, getFlaggedWords().get(word));
		adminNotif.sendWarning();
		
		message.delete();
	}
}