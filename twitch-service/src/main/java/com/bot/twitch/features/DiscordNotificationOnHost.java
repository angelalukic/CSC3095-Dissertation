package com.bot.twitch.features;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.h2.Subscription;
import com.bot.h2.SubscriptionRepository;
import com.bot.twitch.features.beans.TwitchStreamHost;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.HostOnEvent;

@Component
public class DiscordNotificationOnHost {
	
	@Autowired 
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired 
	private DiscordServiceProxy proxy;
	
	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(HostOnEvent.class).subscribe(event -> onHost(event, client));
	}
	
	private void onHost(HostOnEvent event, TwitchClient client) {

		TwitchStreamHost host = new TwitchStreamHost(event, client);
		long id = host.getHostChannel().getId();
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		
		for(int i = 0; i < subscriptions.size(); i++) {
			Subscription subscription = subscriptions.get(i);
			
			if(subscription.getListener().getId() == id) {
				long serverId = subscription.getServer().getId();
				proxy.sendToDiscord(host, serverId);
			}
		}
	}
}
