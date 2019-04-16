package com.bot.subscription.twitter;

import com.bot.twitch.listener.TwitchListener;
import com.bot.twitter.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterSubscription {
	
	private TwitterListener twitter;
	private TwitchListener twitch;
	
	public TwitterSubscription() {
	}
	
	public TwitterSubscription(TwitterListener twitter, TwitchListener twitch) {
		this.twitter = twitter;
		this.twitch = twitch;
	}

}
