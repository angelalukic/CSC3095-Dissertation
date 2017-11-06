package com.bot.filter.words;

import de.btobastian.javacord.entities.message.Message;

public interface Words {
	
	public void addWords();
	
	public void addExceptions();
	
	public String checkWords(String[] input);
	
	public void sendFlaggedWordNotification(Message message, String word);
}
