package com.bot.filter;

import com.bot.filter.response.FilterNotification;
import com.bot.filter.words.HighUrgencyWords;
import com.bot.filter.words.LowUrgencyWords;
import com.bot.filter.words.MediumUrgencyWords;
import com.bot.filter.words.Words;

import de.btobastian.javacord.entities.message.Message;

public class WordFilter {	
	
	private Message message;
	FilterNotification adminNotif;
	FilterNotification userNotif;

	public WordFilter(Message message) {
		this.message = message;
	}
	
	public void checkMessage() {
		
		Filter filter = new Filter();
		String noDupes = filter.removeDuplicateChars(message.toString().toLowerCase());
		String[] inputWords = filter.splitIntoWordArray(noDupes);
		
		checkHighUrgencyWords(inputWords);
		checkMediumUrgencyWords(inputWords);
		checkLowUrgencyWords(inputWords);
		
		if(message.getContent().toLowerCase().contains("genji")
				|| message.getContent().toLowerCase().contains("robotfucker")) {
			message.addCustomEmojiReaction(
					message.getChannelReceiver().getServer().getCustomEmojiByName("RobotFucker"));
		}
	}

	private void checkHighUrgencyWords(String[] inputWords) {
		Words highUrgencyWords = new HighUrgencyWords();
		String flaggedWord = highUrgencyWords.checkWords(inputWords);
		
		if(flaggedWord != null) {
			highUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
	
	private void checkMediumUrgencyWords(String[] inputWords) {
		Words mediumUrgencyWords = new MediumUrgencyWords();
		String flaggedWord = mediumUrgencyWords.checkWords(inputWords);
		
		if(flaggedWord != null) {
			mediumUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
	
	private void checkLowUrgencyWords(String[] inputWords) {
		Words lowUrgencyWords = new LowUrgencyWords();
		String flaggedWord = lowUrgencyWords.checkWords(inputWords);
		
		if(flaggedWord != null) {
			lowUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
}
