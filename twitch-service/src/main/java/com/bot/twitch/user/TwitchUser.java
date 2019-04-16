package com.bot.twitch.user;

import com.github.twitch4j.helix.domain.User;

import lombok.Getter;
import lombok.Setter;

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
	
	public TwitchUser(User user) {
		this.displayName = user.getDisplayName();
		this.id = user.getId();
		this.description = user.getDescription();
		this.image = user.getProfileImageUrl();
		this.viewCount = user.getViewCount();
	}
}
