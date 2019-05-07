package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchGame;
import com.bot.twitch.beans.TwitchStream;
import com.bot.twitch.beans.TwitchUser;

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
