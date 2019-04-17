package com.bot.subscription.twitter;

import com.bot.twitch.listener.TwitchListener;
import com.bot.twitter.listener.TwitterListener;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterSubscription {
	
	private TwitchListener twitch;
	private TwitterListener twitter;
	
	public TwitterSubscription() {
	}
	
	public TwitterSubscription(TwitchListener twitch, TwitterListener twitter) {
		this.twitch = twitch;
		this.twitter = twitter;
	}

}
