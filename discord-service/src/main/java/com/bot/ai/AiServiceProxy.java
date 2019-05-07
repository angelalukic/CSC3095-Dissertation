package com.bot.ai;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.ai.beans.AiMessage;

@FeignClient(name="ai-service", url="localhost:8084")
public interface AiServiceProxy {

	@PostMapping("/ai/message/")
	public String getReplyFromAi(@RequestBody AiMessage message);
}
