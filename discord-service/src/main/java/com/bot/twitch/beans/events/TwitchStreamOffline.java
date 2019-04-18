package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamOffline {
	
	private TwitchUser user;
	
	public TwitchStreamOffline() {
	}
	
	public TwitchStreamOffline(TwitchStreamOffline event) {
		this.user = event.getUser();
	}
}
