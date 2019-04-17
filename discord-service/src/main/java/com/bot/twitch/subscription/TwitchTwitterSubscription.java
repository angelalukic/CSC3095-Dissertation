package com.bot.twitch.subscription;

import com.bot.twitch.TwitchListener;
import com.bot.twitter.TwitterListener;

public class TwitchTwitterSubscription {
	
	private TwitchListener twitchListener;
	private TwitterListener twitterListener;
	
	public TwitchTwitterSubscription(TwitchListener twitchListener, TwitterListener twitterListener) {
		this.twitchListener = twitchListener;
		this.twitterListener = twitterListener;
	}
}
