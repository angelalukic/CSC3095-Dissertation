package com.bot.twitch.events;

import com.bot.twitch.TwitchUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamUnhost {
	
	private TwitchUser hostChannel;
	
	public TwitchStreamUnhost() {
	}
	
	public TwitchStreamUnhost (TwitchStreamUnhost event) {
		this.hostChannel = event.getHostChannel();
	}
}
