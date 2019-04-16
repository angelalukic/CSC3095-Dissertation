package com.bot;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.bot.twitch.features.ChannelNotificationOnCheer;
import com.bot.twitch.features.ChannelNotificationOnDonation;
import com.bot.twitch.features.ChannelNotificationOnFollow;
import com.bot.twitch.features.ChannelNotificationOnSubscription;
import com.bot.twitch.features.DiscordNotificationOnHost;
import com.bot.twitch.features.DiscordNotificationOnLive;
import com.bot.twitch.features.DiscordNotificationOnOffline;
import com.bot.twitch.features.WriteChannelChatToDiscord;
import com.bot.twitch.listener.TwitchListener;
import com.bot.twitch.listener.TwitchListenerRepository;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
	
	@Autowired private Configuration configuration;
	@Autowired private TwitchListenerRepository listenerRepository;
	@Autowired private ChannelNotificationOnCheer cheerNotification;
	@Autowired private ChannelNotificationOnDonation donationNotification;
	@Autowired private ChannelNotificationOnFollow followNotification;
	@Autowired private ChannelNotificationOnSubscription subscriptionNotification;
	@Autowired private DiscordNotificationOnHost hostNotification;
	@Autowired private DiscordNotificationOnLive liveNotification;
	@Autowired private DiscordNotificationOnOffline offlineNotification;
	@Autowired private WriteChannelChatToDiscord chatNotification;
	private TwitchClient client;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
		OAuth2Credential credential = new OAuth2Credential("twitch", configuration.getIrc());
		client = clientBuilder
				.withClientId(configuration.getId())
				.withClientSecret(configuration.getSecret())
				.withEnableHelix(true)
				.withChatAccount(credential)
				.withEnableChat(true)
				.build();
		registerFeatures();
		connectToChannels();
	}
	
	private void registerFeatures() {
		EventManager manager = client.getEventManager();
		cheerNotification.register(manager);
		donationNotification.register(manager);
		followNotification.register(manager);
		subscriptionNotification.register(manager);
		hostNotification.register(manager, client);
		liveNotification.register(manager, client);
		offlineNotification.register(manager, client);
		chatNotification.register(manager, client);
	}

	private void connectToChannels() {
		List<TwitchListener> listeners = listenerRepository.findAll();
		for(int i = 0; i < listeners.size(); i++) {
			String channel = listeners.get(i).getName();
			listen(channel);
		}
	}
	
	private void listen(String channel) {
		client.getChat().joinChannel(channel);
		//client.getClientHelper().enableStreamEventListener(channel);
		//client.getClientHelper().enableFollowEventListener(channel);
	}
}
