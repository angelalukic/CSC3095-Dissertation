package com.bot.twitch.features.beans;

import com.bot.twitch.user.TwitchUser;
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
