	package com.bot.twitch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.twitch.features.ChannelNotificationOnCheer;
import com.bot.twitch.features.ChannelNotificationOnDonation;
import com.bot.twitch.features.ChannelNotificationOnFollow;
import com.bot.twitch.features.ChannelNotificationOnSubscription;
import com.bot.twitch.features.discord.DiscordNotificationOnHost;
import com.bot.twitch.features.discord.DiscordNotificationOnLive;
import com.bot.twitch.features.discord.DiscordNotificationOnOffline;
import com.bot.twitch.features.discord.WriteChannelChatToDiscord;
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.TwitchClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TwitchChannelConnection {
	
	@Autowired private ChannelNotificationOnCheer cheerNotification;
	@Autowired private ChannelNotificationOnDonation donationNotification;
	@Autowired private ChannelNotificationOnFollow followNotification;
	@Autowired private ChannelNotificationOnSubscription subscriptionNotification;
	@Autowired private DiscordNotificationOnHost hostNotification;
	@Autowired private DiscordNotificationOnLive liveNotification;
	@Autowired private DiscordNotificationOnOffline offlineNotification;
	@Autowired private WriteChannelChatToDiscord chatNotification;
	@Autowired private Configuration configuration;
	private TwitchClient client;
	
	public void buildClient(TwitchClientBuilder clientBuilder) {
		OAuth2Credential credential = new OAuth2Credential("twitch", configuration.getIrc());
		client = clientBuilder
				.withClientId(configuration.getId())
				.withClientSecret(configuration.getSecret())
				.withEnableHelix(true)
				.withEnableChat(true)
				.withChatAccount(credential)
				.withTimeout(120*1000)
				.build();
	}
	
	public void registerFeatures() {
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
	
	public void listenToChannel(String username) {
		log.info("[" + username + "] Joining Channel and Enabling StreamEvent and FollowEvent listeners.");
		client.getChat().joinChannel(username);
		client.getClientHelper().enableStreamEventListener(username);
		client.getClientHelper().enableFollowEventListener(username);
	}
}
