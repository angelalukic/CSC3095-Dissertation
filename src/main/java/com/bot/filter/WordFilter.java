package com.bot.filter;

import java.util.Arrays;
import java.util.Map;

import com.bot.filter.response.AdminFilterNotification;
import com.bot.filter.response.FilterNotification;
import com.bot.filter.response.UserFilterNotification;
import com.bot.filter.words.HighUrgencyWords;
import com.bot.filter.words.LowUrgencyWords;
import com.bot.filter.words.MediumUrgencyWords;
import com.bot.filter.words.Words;

import de.btobastian.javacord.entities.message.Message;

public class WordFilter {	
	
	private Message message;
	private Map<String, String> flaggedWords;
	FilterNotification adminNotif;
	FilterNotification userNotif;
	Words highUrgencyWords;
	Words mediumUrgencyWords;
	Words lowUrgencyWords;

	public WordFilter(Message message) {
		this.message = message;
	}
	
	public void checkMessage() {
		
		Filter filter = new Filter();
		String noDupes = filter.removeDuplicateChars(message.toString().toLowerCase());
		String[] inputWords = filter.splitIntoWordArray(noDupes);
		
		checkHighUrgencyWords(new HighUrgencyWords(), inputWords);
		checkMediumUrgencyWords(new MediumUrgencyWords(), inputWords);
		checkLowPriorityWords(new LowUrgencyWords(), inputWords);
		
		if(message.getContent().toLowerCase().contains("genji")
				|| message.getContent().toLowerCase().contains("robotfucker")) {
			message.addCustomEmojiReaction(
					message.getChannelReceiver().getServer().getCustomEmojiByName("RobotFucker"));
		}
	}

	private void checkHighUrgencyWords(HighUrgencyWords words, String[] inputWords) {
		
		flaggedWords = words.checkWords(Arrays.asList(inputWords));
		
		if(!flaggedWords.isEmpty()) {
			
			for(String word : flaggedWords.keySet()) {
				
				if(words.checkExceptions(word) != null) {
					
					message.delete();
					
					userNotif = new UserFilterNotification(message, flaggedWords.get(word));
					userNotif.sendWarning();
					
					adminNotif = new AdminFilterNotification(message, flaggedWords.get(word));
					adminNotif.sendWarning();
				}				
			}
		}
	}
	
	private void checkMediumUrgencyWords(MediumUrgencyWords words, String[] inputWords) {
		
		flaggedWords = words.checkWords(Arrays.asList(inputWords));
		
		if(!flaggedWords.isEmpty()) {
			
			for(String word : flaggedWords.keySet()) {
				
				if(words.checkExceptions(word) != null) {
					
					userNotif = new UserFilterNotification(message, flaggedWords.get(word));
					userNotif.sendCaution();
					
					adminNotif = new AdminFilterNotification(message, flaggedWords.get(word));
					adminNotif.sendCaution();
				}
			}		
		}
	}
	
	private void checkLowPriorityWords(LowUrgencyWords words, String[] inputWords) {

		flaggedWords = words.checkWords(Arrays.asList(inputWords));
		
		if(!flaggedWords.isEmpty()) {
			
			for(String word : flaggedWords.keySet()) {
				
				if(words.checkExceptions(word) != null) {
			
					adminNotif = new AdminFilterNotification(message, flaggedWords.get(word));
					adminNotif.sendNotification();
				}	
			}
		}
	}
}
