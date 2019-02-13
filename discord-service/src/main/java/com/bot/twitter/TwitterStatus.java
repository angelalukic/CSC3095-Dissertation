package com.bot.twitter;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class TwitterStatus {
	
	private TwitterUser user;
	private long id;
	private String body;
	private Date date;
	private String image;
	private String video;
	private boolean isRetweet;
	
	public TwitterStatus() {
	}
	
	public TwitterStatus(TwitterStatus status) {
		this.user = status.getUser();
		this.id = status.getId();
		this.body = status.getBody();
		this.date = status.getDate();
		this.image = status.getImage();
		this.video = status.getVideo();
		this.isRetweet = status.isRetweet();
	}
}