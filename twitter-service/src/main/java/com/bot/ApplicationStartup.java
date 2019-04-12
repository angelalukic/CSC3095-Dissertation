package com.bot;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.subscription.SubscriptionDAO;
import com.bot.twitter.TwitterStreamConnection;
import com.bot.twitter.listener.TwitterListener;
import com.bot.twitter.listener.TwitterListenerDTO;
import com.bot.twitter.listener.TwitterListenerRepository;
import com.bot.twitter.listener.buffer.TwitterListenerBuffer;
import com.bot.twitter.listener.buffer.TwitterListenerBufferRepository;

import lombok.extern.slf4j.Slf4j;

/*
 * onApplicationEvent executes once the Spring Boot application has started
 * http://blog.netgloo.com/2014/11/13/run-code-at-spring-boot-startup/ 
 */

@Slf4j
@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private TwitterListenerRepository listenerRepository;
	@Autowired private TwitterListenerBufferRepository buffer;
	@Autowired private DiscordServiceProxy proxy;
	@Autowired private SubscriptionDAO subscription;
	
	@Override
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		List<TwitterListener> listeners = listenerRepository.findAll();
		long[] ids = new long[listeners.size()];
		for(int  i = 0; i < listeners.size(); i++) {
			long id = listeners.get(i).getId();
			ids[i] = id;
			log.info("Adding Twitter ID " + id + " to filter.");
		}
		TwitterStreamConnection connection = new TwitterStreamConnection(proxy, subscription);
		connection.addFilter(ids);
		startBufferSchedule(connection);
	}
	
	/*
	 * Twitter API will throw error if you try to connect to it too often
	 * When a new listener is added, add it to a buffer which connects every five minutes
	 */
	private void startBufferSchedule(TwitterStreamConnection connection) {
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				List<TwitterListenerBuffer> listeners = buffer.findAll();
				log.info("Timer Task Executing. There are currently " + listeners.size() + " listeners in the buffer.");
				if(!listeners.isEmpty()) {
					addToFilter(listeners);
				}
			}

			private void addToFilter(List<TwitterListenerBuffer> listeners) {
				long[] ids = new long[listeners.size()];
				for(int i = 0; i < listeners.size(); i++) {
					TwitterListenerBuffer listenerBuffer = listeners.get(i);
					TwitterListener listener = new TwitterListener(listenerBuffer);
					log.info("Timer task executing for " + listener.getName());
					ids[i] = listenerBuffer.getTwitterId();
					buffer.delete(listenerBuffer);
					TwitterListenerDTO listenerDTO = new TwitterListenerDTO(listener);
					proxy.sendToDiscord(listenerDTO, listenerBuffer.getDiscordId());
				}
				connection.addFilter(ids);
			}
		};
		
		Timer timer = new Timer("Timer");
		long delay = 6*5*2000L; // 5 minutes
		long period = 6*5*2000L; // 5 minutes
		timer.schedule(task, delay, period);
		log.info("Timer Task has been scheduled");
	}
}
