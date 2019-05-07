package com.bot.twitch.beans.events;

import com.bot.twitch.beans.TwitchGame;
import com.bot.twitch.beans.TwitchStream;
import com.bot.twitch.beans.TwitchUser;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;

import lombok.Setter;

import lombok.Getter;

@Getter
@Setter
public class TwitchStreamHost {
	
	private TwitchUser hostChannel;
	private TwitchUser targetChannel;
	private TwitchStream stream;
	private TwitchGame game;
	
	public TwitchStreamHost() {
	}
	
	public TwitchStreamHost(User hostChannel, User targetChannel, Stream stream, Game game) {
		this.hostChannel = new TwitchUser(hostChannel);
		this.targetChannel = new TwitchUser(targetChannel);
		this.stream = new TwitchStream(stream);
		this.game = new TwitchGame(game);
	}
}
