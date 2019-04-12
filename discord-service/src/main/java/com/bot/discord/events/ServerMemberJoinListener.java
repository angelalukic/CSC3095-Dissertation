package com.bot.discord.events;

import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;

import com.bot.discord.server.DiscordServerRepository;

public class ServerMemberJoinListener {
	
	@Autowired
	private DiscordServerRepository repository;
	
	public void execute(ServerMemberJoinEvent event) {
		
	}

}
