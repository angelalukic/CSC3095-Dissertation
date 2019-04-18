package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchGame;
import com.bot.twitch.beans.TwitchStream;
import com.bot.twitch.beans.TwitchUser;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamLive {
	
	private String title;
	private TwitchUser user;
	private TwitchStream stream;
	private TwitchGame game;
	
	public TwitchStreamLive(String title, User user, Stream stream, Game game) {
		this.title = title;
		this.user = new TwitchUser(user);	
		this.stream = new TwitchStream(stream);
		this.game = new TwitchGame(game);
	}
}
