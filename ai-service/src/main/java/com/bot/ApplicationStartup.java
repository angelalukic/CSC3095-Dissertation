package com.bot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.ai.ArtificialIntelligence;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired ArtificialIntelligence ai;
	
	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		ai.createChatBot();
	}
}
