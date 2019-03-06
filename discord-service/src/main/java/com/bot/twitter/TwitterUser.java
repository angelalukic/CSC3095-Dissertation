package com.bot.twitter;

import lombok.Getter;
import lombok.Setter;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class TwitterUser {
	
	private long id;
	private String name;
	private String screenName;
	private String url;
	private String description;
	private int followers;
	private String profilePicture;
	private String profileBanner;
	
	protected TwitterUser() {
	}
	
	public TwitterUser(TwitterUser user) {
		this.id = user.getId();
		this.name = user.getName();
		this.screenName = user.getScreenName();
		this.url = user.getUrl();
		this.description = user.getDescription();
		this.followers = user.getFollowers();
		this.profilePicture = user.getProfilePicture();
		this.profileBanner = user.getProfileBanner();
	}
}
