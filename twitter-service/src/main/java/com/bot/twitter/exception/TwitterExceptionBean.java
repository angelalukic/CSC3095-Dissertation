package com.bot.twitter.exception;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import twitter4j.TwitterException;

@Getter
@Setter
public class TwitterExceptionBean {
	
	private Date timestamp;
	private int statusCode;
	private int errorCode;
	private String errorMessage;
	private String description;
	private String request;
	
	public TwitterExceptionBean(TwitterException exception, String request) {
		this.timestamp = new Date();
		this.statusCode = exception.getStatusCode();
		this.errorCode = exception.getErrorCode();
		this.errorMessage = exception.getErrorMessage();
		this.description = exception.getMessage();
		this.request = request;
	}
}
