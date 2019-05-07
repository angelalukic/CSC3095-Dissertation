package com.bot.twitter.beans;

import com.bot.discord.beans.server.DiscordServerDTO;
import com.bot.twitter.beans.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterSubscription {
	
	private DiscordServerDTO server;
	private TwitterListener listener;
	
	public TwitterSubscription() {
	}

	public TwitterSubscription(TwitterListener listener, DiscordServerDTO server) {
		this.listener = listener;
		this.server = server;
	}
}
