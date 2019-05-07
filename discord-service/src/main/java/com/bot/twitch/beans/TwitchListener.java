package com.bot.twitch.beans;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchListener {
	
	private long id;
	private String name;
	
	public TwitchListener(long id, String name) {
		this.id = id;
		this.name = name;
	}
}
