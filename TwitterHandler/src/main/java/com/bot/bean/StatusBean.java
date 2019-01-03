package com.bot.bean;

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
public class StatusBean {
	
	public StatusBean(Status status) {
		this.id = status.getId();
		this.body = status.getText();
		this.date = status.getCreatedAt();
		this.userId = status.getUser().getId();
		this.isRetweet = status.isRetweet();
	}
	
	private long id;
	private String body;
	private Date date;
	private long userId;
	private boolean isRetweet;
}
