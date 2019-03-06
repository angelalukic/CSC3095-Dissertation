package com.bot.twitch;

import lombok.Setter;
import lombok.Getter;

@Getter
@Setter
public class TwitchUser {
	
	private String displayName;
	private long id;
	private String description;
	private String image;
	private int viewCount;
	
	public TwitchUser() {
	}
	
	public TwitchUser(TwitchUser user) {
		this.displayName = user.getDisplayName();
		this.id = user.getId();
		this.description = user.getDescription();
		this.image = user.getImage();
		this.viewCount = user.getViewCount();
	}
}
