package com.bot.filter.words;

import java.util.List;

import org.javacord.api.entity.message.Message;

public interface Words {
	
	public List<String> retrieveFlaggedWords(Message message);	
}
