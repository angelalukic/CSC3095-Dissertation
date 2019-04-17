package com.bot.twitter.listener;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.bot.twitch.listener.TwitchListener;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TwitterListener {
	
	@Id
	private long id;
	private String name;
	
	@ManyToMany(mappedBy = "twitterListeners")
	@JsonIgnore
	private Set<TwitchListener> twitchListeners;
	
	public TwitterListener() {
	}
	
	public TwitterListener(long id, String name, Set<TwitchListener> twitchListeners) {
		this.id = id;
		this.name = name;
		this.twitchListeners = twitchListeners;
	}
}
