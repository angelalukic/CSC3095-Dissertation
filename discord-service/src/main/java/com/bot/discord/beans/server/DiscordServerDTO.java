package com.bot.discord.beans.server;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordServerDTO {
	
	private long id;
	private String name;
	
	public DiscordServerDTO() {
	}
	
	public DiscordServerDTO(long id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public DiscordServerDTO(DiscordServer server) {
		this.id = server.getId();
		this.name = server.getName();
	}
}
