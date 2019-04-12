package com.bot.twitter.listener.buffer;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

import com.bot.discord.server.DiscordServer;
import com.bot.twitter.listener.TwitterListener;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@Entity
@IdClass(TwitterListenerBufferId.class)
public class TwitterListenerBuffer {
	
	@Id
	private long twitterId;
	
	@Id	
	private long discordId;
	
	private String twitterName;
	
	public TwitterListenerBuffer() {
	}
	
	public TwitterListenerBuffer(TwitterListener listener, DiscordServer server) {
		this.twitterId = listener.getId();
		this.discordId = server.getId();
		this.twitterName = listener.getName();
	}
}
