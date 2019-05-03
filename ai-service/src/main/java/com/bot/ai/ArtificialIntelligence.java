package com.bot.ai;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.History;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ArtificialIntelligence {
	
	@Autowired private Configuration configuration;	
	private Chat chatSession;
	
	public void createChatBot() {
		String resourcesPath = configuration.getResourcesPath();
		log.info(resourcesPath);
		Bot bot = new Bot("super", resourcesPath);
		bot.writeAIMLFiles();
		chatSession = new Chat(bot);
		bot.brain.nodeStats();
	}
	
	public void startChatSession() {
		
	}
	
	public String getMessageResponse(String message) {
		return chatSession.multisentenceRespond(message);
	}
}
