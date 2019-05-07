package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchUser;
import com.github.twitch4j.helix.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamOffline {
	
	private TwitchUser user;
	
	public TwitchStreamOffline() {
	}
	
	public TwitchStreamOffline(User user) {
		this.user = new TwitchUser(user);
	}
}
