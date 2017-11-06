package com.bot.filter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.bot.filter.response.AdminFilterNotification;
import com.bot.filter.response.UserFilterNotification;

import de.btobastian.javacord.entities.message.Message;

public class WordFilter {	
	
	private Message message;
	private String[] input;
	private Map<String, String> flaggedWords;
	AdminFilterNotification adminNotif;
	UserFilterNotification userNotif;
	Words highUrgencyWords;
	Words mediumUrgencyWords;
	Words lowUrgencyWords;

	public WordFilter(Message message) {
		this.message = message;
		this.input = message.getContent().toLowerCase().split("\\s+");
	}
	
	public void checkMessage() {
		
		checkHighUrgencyWords(new HighUrgencyWords());
		checkMediumUrgencyWords(new MediumUrgencyWords());
		checkLowPriorityWords(new LowUrgencyWords());
		
		if(message.getContent().toLowerCase().contains("genji")
				|| message.getContent().toLowerCase().contains("robotfucker")) {
			message.addCustomEmojiReaction(
					message.getChannelReceiver().getServer().getCustomEmojiByName("RobotFucker"));
		}
	}

	private void checkHighUrgencyWords(HighUrgencyWords words) {
		
		flaggedWords = words.checkWords(Arrays.asList(input));
		
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
	
	private void checkMediumUrgencyWords(MediumUrgencyWords words) {
		
		flaggedWords = words.checkWords(Arrays.asList(input));
		
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
	
	private void checkLowPriorityWords(LowUrgencyWords words) {

		flaggedWords = words.checkWords(Arrays.asList(input));
		
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
