package com.bot.twitch.features.beans;

import com.bot.twitch.game.TwitchGame;
import com.bot.twitch.stream.TwitchStream;
import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.HostOnEvent;

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
	
	public TwitchStreamHost(HostOnEvent event, TwitchClient client) {
		this.hostChannel = new TwitchUser(event.getChannel(), client);
		this.targetChannel = new TwitchUser(event.getTargetChannel(), client);
		this.stream = new TwitchStream(targetChannel, client);
		this.game = stream.getGame();
	}
}
