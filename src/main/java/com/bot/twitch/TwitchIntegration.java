package com.bot.twitch;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import com.bot.twitch.eventlisteners.NotificationOnHost;
import com.bot.twitch.eventlisteners.NotificationOnUnhost;

import de.btobastian.javacord.entities.message.Message;
import lombok.Setter;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.TwitchClientBuilder;
import me.philippheuer.twitch4j.endpoints.ChannelEndpoint;

public class TwitchIntegration {
	
	@Setter private Message message;
	
	private static final String CLIENT_ID = "3r70uj12i74tnn86ibirq23tvq0fjy";
	private static final String CLIENT_SECRET = "ccqb4bioa103m7qw87tup7snvx1cgf";
	private static final String CREDENTIALS = "t1r39ljpa2gqw96h6w5ww1on00oe8h";
	private static final String USERNAME = "nclgamingsociety";
	
	public TwitchIntegration(Message message) {
		this.message = message;
	}
	
	public void connect() {
		
		TwitchClient client = TwitchClientBuilder.init()
                .withClientId(CLIENT_ID)
                .withClientSecret(CLIENT_SECRET)
                .withCredential(CREDENTIALS)
                .withAutoSaveConfiguration(true)
                .withConfigurationDirectory(new File("config"))
                .connect();
		
		registerListeners(client);
		connectToChannel(client);
		checkIfLive(client);
	}
	
	private void registerListeners(TwitchClient client) {
		client.getDispatcher().registerListener(this);
		client.getDispatcher().registerListener(new NotificationOnHost());
		client.getDispatcher().registerListener(new NotificationOnUnhost());
	}
	
	private void connectToChannel(TwitchClient client) {
		Long channelId = client.getUserEndpoint().getUserIdByUserName(USERNAME).get();
		ChannelEndpoint channelEndpoint = client.getChannelEndpoint(channelId);
		channelEndpoint.registerEventListener();
	}
	
	private void checkIfLive(TwitchClient client) {
		
		Timer timer = new Timer();		
		TimerTask task = new StreamChecker(message, client, USERNAME);
		timer.schedule(task, 0, 60000);
	}
}
