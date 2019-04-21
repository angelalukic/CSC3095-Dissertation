package com.filter.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.filter.message.discord.DiscordMessage;
import com.filter.message.twitch.TwitchMessage;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MessageController {

	@Autowired private MessageDAO service;
	
	@PostMapping("/filter/twitch/{channel}")
	public MessageJudgement checkTwitchMessage(@RequestBody TwitchMessage message, @PathVariable long channel) {
		log.debug("POST localhost:8083/filter/twitch/{channel}");
		return service.assessMessage(message, channel);
	}
	
	@PostMapping("/filter/discord/{channel}")
	public MessageJudgement checkDiscordMessage(@RequestBody DiscordMessage message, @PathVariable long channel) {
		log.debug("POST localhost:8083/filter/discord/{channel}");
		return service.assessMessage(message, channel);
	}
}
