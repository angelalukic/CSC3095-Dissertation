package com.bot.app;

import com.bot.twitch.TwitchIntegration;
import com.bot.twitter.TwitterIntegration;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

public class BotApp {
	
	private static boolean connectedToSocialMedia;

	public static void main(String[] args) {

		DiscordAPI api = Javacord.getApi("", true);
		api.connectBlocking();

		api.registerListener(new MessageCreateListener() {
			public void onMessageCreate(DiscordAPI api, Message message) {
				
				Command command = new Command(message);
				command.execute();
				
				if(!connectedToSocialMedia) {
					connectedToSocialMedia = true;
					
					TwitterIntegration twitter = new TwitterIntegration(message);
					twitter.retrieveStream();
					
					TwitchIntegration twitch = new TwitchIntegration(message);
					twitch.connect();
				}
			}
		});
	}
}
