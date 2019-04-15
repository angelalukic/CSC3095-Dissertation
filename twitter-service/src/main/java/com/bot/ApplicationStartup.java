package com.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.twitter.TwitterStreamConnection;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Slf4j
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private TwitterListenerRepository listenerRepository;
	@Autowired TwitterStreamConnection connection;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		List<TwitterListener> listeners = listenerRepository.findAll();
		long[] ids = new long[listeners.size()];
		for(int  i = 0; i < listeners.size(); i++) {
			long id = listeners.get(i).getId();
			ids[i] = id;
			log.info("Adding Twitter ID " + id + " to filter.");
		}
		connection.connectToStream();
		connection.addToFilter(ids);
	}
}
