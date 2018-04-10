package com.bot.filter;

import com.bot.filter.words.HighUrgencyWords;
import com.bot.filter.words.LowUrgencyWords;
import com.bot.filter.words.MediumUrgencyWords;
import com.bot.filter.words.Words;

import de.btobastian.javacord.entities.message.Message;

public class WordFilter {	
	
	private Message message;

	public WordFilter(Message message) {
		this.message = message;
	}
	
	public void checkMessage() {
		
		String messageContent = message.getContent().toLowerCase();
		
		Filter filter = new Filter();
		String noDupes = filter.removeDuplicateChars(messageContent);
		String cleanInput = filter.removeSymbols(noDupes);
		
		checkHighUrgencyWords(cleanInput);
		checkMediumUrgencyWords(cleanInput);
		checkLowUrgencyWords(cleanInput);
		
		if (messageContent.contains("genji") || messageContent.contains("robotfucker")
				|| messageContent.contains("dragonblade")) {
			message.addCustomEmojiReaction(
					message.getChannelReceiver().getServer().getCustomEmojiByName("robotfucker"));
		}
	}

	private void checkHighUrgencyWords(String input) {
		Words highUrgencyWords = new HighUrgencyWords();
		String flaggedWord = highUrgencyWords.checkWords(input);
		
		if(flaggedWord != null) {
			highUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
	
	private void checkMediumUrgencyWords(String input) {
		Words mediumUrgencyWords = new MediumUrgencyWords();
		String flaggedWord = mediumUrgencyWords.checkWords(input);
		
		if(flaggedWord != null) {
			mediumUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
	
	private void checkLowUrgencyWords(String input) {
		Words lowUrgencyWords = new LowUrgencyWords();
		String flaggedWord = lowUrgencyWords.checkWords(input);
		
		if(flaggedWord != null) {
			lowUrgencyWords.sendFlaggedWordNotification(message, flaggedWord);
		}
	}
}
