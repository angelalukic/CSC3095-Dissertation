package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchGame;
import com.bot.twitch.beans.TwitchStream;
import com.bot.twitch.beans.TwitchUser;

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
