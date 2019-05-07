package com.bot.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.ai.beans.AiMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ArtificialIntelligenceController {
	
	@Autowired ArtificialIntelligence ai;

	@PostMapping("/ai/message/")
	public String createSubscription(@RequestBody AiMessage message) {
		log.info("POST localhost:8084/ai/message");	
		return ai.getMessageResponse(message.getContent());
	}
}
