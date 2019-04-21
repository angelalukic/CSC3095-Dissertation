package com.bot.filter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.discord.beans.DiscordMessage;
import com.bot.filter.beans.MessageJudgement;

@FeignClient(name="filter-service", url="localhost:8083")
public interface FilterServiceProxy {
	
	@PostMapping("/filter/discord/{channel}")
	public MessageJudgement checkMessage(@RequestBody DiscordMessage message, @PathVariable("channel") long channel);
}