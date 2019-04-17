package com.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.twitch.TwitchChannelConnection;
import com.bot.twitch.listener.TwitchListener;
import com.bot.twitch.listener.TwitchListenerRepository;
import com.github.twitch4j.TwitchClientBuilder;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private TwitchChannelConnection connection;
	@Autowired private TwitchListenerRepository listenerRepository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
		connection.buildClient(clientBuilder);
		connection.registerFeatures();
		connectToChannelsInRepository();
	}
	
	private void connectToChannelsInRepository() {
		List<TwitchListener> listeners = listenerRepository.findAll();
		for(int i = 0; i < listeners.size(); i++) {
			String channel = listeners.get(i).getName();
			connection.listenToChannel(channel);
		}
	}
}
