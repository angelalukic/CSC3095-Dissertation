package com.bot.twitch.events;

import com.bot.twitch.TwitchGame;
import com.bot.twitch.TwitchStream;
import com.bot.twitch.TwitchUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamLive {
	
	private String title;
	private TwitchUser user;
	private TwitchStream stream;
	private TwitchGame game;
	
	public TwitchStreamLive() {
	}
	
	public TwitchStreamLive(TwitchStreamLive event) {
		this.title = event.getTitle();
		this.user = event.getUser();
		this.stream = event.getStream();
		this.game = event.getGame();
	}
}
