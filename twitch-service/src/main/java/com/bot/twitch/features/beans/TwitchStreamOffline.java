package com.bot.twitch.features.beans;

import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoOfflineEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamOffline {
	
	private TwitchUser user;
	
	public TwitchStreamOffline() {
	}
	
	public TwitchStreamOffline(ChannelGoOfflineEvent event, TwitchClient client) {
		this.user = new TwitchUser(event.getChannel(), client);
	}
}
