package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchUser;

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
