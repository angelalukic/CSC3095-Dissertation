package com.bot.twitch.features.beans;

import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.HostOffEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamUnhost {
	
	private TwitchUser hostChannel;
	
	public TwitchStreamUnhost() {
	}
	
	public TwitchStreamUnhost (HostOffEvent event, TwitchClient client) {
		this.hostChannel = new TwitchUser(event.getChannel(), client);
	}
}
