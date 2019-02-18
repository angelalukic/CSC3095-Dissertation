package com.bot.discord.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class DiscordExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ChannelNotFoundException.class)
	public final ResponseEntity<Object> handleChannelNotFoundException(ChannelNotFoundException ex, WebRequest request) {
		DiscordException exceptionResponse = new DiscordException(new Date(), ex.getMessage(), "Channel Not Found");
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(ServerNotFoundException.class)
	public final ResponseEntity<Object> handleServerNotFoundException(ServerNotFoundException ex, WebRequest request) {
		DiscordException exceptionResponse = new DiscordException(new Date(), ex.getMessage(), "Server Not Found");
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}
	
}
