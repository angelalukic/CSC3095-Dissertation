package com.bot.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bot.filter.beans.MessageJudgement;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FilterController {
	
	@Autowired private FilterDAO service;
	
	//@PostMapping("/filter/twitch/{channel}")
	//public ResponseEntity<Object> reportTwitchMessage(@RequestBody MessageJudgement message, @PathVariable long channel) {
	//	log.info("POST localhost:8080/filter/twitch/{channel}");
	//	service.postToDiscord(message, channel);
	//}
}
