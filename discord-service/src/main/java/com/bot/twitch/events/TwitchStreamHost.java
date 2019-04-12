package com.bot.twitch.events;

import com.bot.twitch.TwitchGame;
import com.bot.twitch.TwitchStream;
import com.bot.twitch.TwitchUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamHost {
	
	private TwitchUser hostChannel;
	private TwitchUser targetChannel;
	private TwitchStream stream;
	private TwitchGame game;
	
	public TwitchStreamHost() {
	}
	
	public TwitchStreamHost(TwitchStreamHost event) {
		this.hostChannel = event.getHostChannel();
		this.targetChannel = event.getTargetChannel();
		this.stream = event.getStream();
		this.game = event.getGame();
	}
}
