package com.bot.twitter.beans.listener;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitterListener {
	
	private long id;
	private String name;
	
	public TwitterListener() {
	}
	
	public TwitterListener(long id, String name) {
		this.id = id;
		this.name = name;
	}
}
