package com.bot.twitch.features.beans;

import com.bot.twitch.game.TwitchGame;
import com.bot.twitch.stream.TwitchStream;
import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.channel.ChannelGoLiveEvent;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStreamLive {
	
	private String title;
	private TwitchUser user;
	private TwitchStream stream;
	private TwitchGame game;
	
	public TwitchStreamLive(ChannelGoLiveEvent event, TwitchClient client) {
		this.title = event.getTitle();
		this.user = new TwitchUser(event.getChannel(), client);	
		this.stream = new TwitchStream(user, client);
		this.game = stream.getGame();
	}
}
