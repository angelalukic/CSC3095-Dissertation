package com.bot.filter.words;

import java.util.List;

public interface Words {
	
	public List<String> retrieveFlaggedWords();	
	
	public void sendNotification();
}
