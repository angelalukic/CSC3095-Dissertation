package com.bot.twitch.features.beans;

import com.bot.twitch.game.TwitchGame;
import com.bot.twitch.stream.TwitchStream;
import com.bot.twitch.user.TwitchUser;
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
