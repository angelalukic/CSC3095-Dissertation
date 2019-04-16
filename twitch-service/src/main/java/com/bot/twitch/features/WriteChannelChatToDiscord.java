package com.bot.twitch.features;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordServiceProxy;
import com.bot.h2.Subscription;
import com.bot.h2.SubscriptionRepository;
import com.bot.twitch.features.beans.TwitchChatMessage;
import com.github.philippheuer.events4j.EventManager;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;

@Component
public class WriteChannelChatToDiscord {
	
	@Autowired 
	private SubscriptionRepository subscriptionRepository;
	
	@Autowired 
	private DiscordServiceProxy proxy;

	public void register(EventManager eventManager, TwitchClient client) {
		eventManager.onEvent(ChannelMessageEvent.class).subscribe(event -> onChannelMessage(event, client));
	}
	
	private void onChannelMessage(ChannelMessageEvent event, TwitchClient client) {
		
		TwitchChatMessage message = new TwitchChatMessage(event, client);
		long id = message.getChannel().getId();
		List<Subscription> subscriptions = subscriptionRepository.findAll();
		
		for(int i = 0; i < subscriptions.size(); i++) {
			Subscription subscription = subscriptions.get(i);
			
			if(subscription.getListener().getId() == id) {
				long serverId = subscription.getServer().getId();
				proxy.sendToDiscord(message, serverId);
			}
		}
		
	}
}
