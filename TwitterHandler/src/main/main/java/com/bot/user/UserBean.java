package com.bot.user;

import lombok.Getter;
import lombok.Setter;
import twitter4j.User;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class UserBean {
	
	public UserBean(User user) {
		this.id = user.getId();
		this.name = user.getName();
		this.screenName = user.getScreenName();
		this.url = user.getURL();
		this.description = user.getDescription();
		this.followers = user.getFollowersCount();
		this.profilePicture = user.getProfileImageURL();
		this.profileBanner = user.getProfileBannerURL();
	}
	
	private long id;
	private String name;
	private String screenName;
	private String url;
	private String description;
	private int followers;
	private String profilePicture;
	private String profileBanner;
}
