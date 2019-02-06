package com.bot.twitter;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import twitter4j.Status;

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
	private boolean isRetweet;
	
	public TwitterStatus() {
	}
	
	public TwitterStatus(Status status) {
		this.user = new TwitterUser(status.getUser());
		this.id = status.getId();
		this.body = status.getText();
		this.date = status.getCreatedAt();
		this.isRetweet = status.isRetweet();
	}
}