package com.bot.twitter.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import twitter4j.TwitterException;

@ControllerAdvice
@RestController
public class TwitterExceptionController extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(TwitterException.class)
	public final ResponseEntity<Object> handleTwitterException(TwitterException ex, WebRequest request) {
		TwitterExceptionBean response = new TwitterExceptionBean(ex, request.getDescription(false));
		
		if(ex.getStatusCode()==404)
			return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
