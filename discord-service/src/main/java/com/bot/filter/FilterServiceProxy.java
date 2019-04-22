package com.bot.filter;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.bot.discord.beans.DiscordMessage;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.filter.beans.MessageJudgement;

@FeignClient(name="filter-service", url="localhost:8083")
public interface FilterServiceProxy {
	
	@PostMapping("/filter/discord/{channel}")
	public MessageJudgement checkMessage(@RequestBody DiscordMessage message, @PathVariable("channel") long channel);

	@PostMapping("/filter/discord/blacklist/add/{word}")
	public ResponseEntity<Object> addToBlacklist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/discord/blacklist/remove/{word}")
	public ResponseEntity<Object> removeFromBlacklist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/discord/greylist/add/{word}")
	public ResponseEntity<Object> addToGreylist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/discord/greylist/remove/{word}")
	public ResponseEntity<Object> removeFromGreylist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/discord/whitelist/add/{word}")
	public ResponseEntity<Object> addToWhitelist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/discord/whitelist/remove/{word}")
	public ResponseEntity<Object> removeFromWhitelist(@RequestBody DiscordServer server, @PathVariable("word") String word);
	
	@PostMapping("/filter/twitch/sync/{channel}")
	public ResponseEntity<Object> syncToTwitchChannel(@RequestBody DiscordServer server, @PathVariable("channel") long channel);
}