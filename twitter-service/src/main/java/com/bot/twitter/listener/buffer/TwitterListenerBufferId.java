package com.bot.twitter.listener.buffer;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterListenerBufferId implements Serializable {
	
	private long twitterId;
	private long discordId;
}
