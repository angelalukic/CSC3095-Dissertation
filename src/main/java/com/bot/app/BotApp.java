package com.bot.app;

import com.bot.filter.WordFilter;
import com.bot.twitter.TwitterIntegration;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

public class BotApp {

	public static void main(String[] args) {

		DiscordAPI api = Javacord.getApi(/**/, true);
		api.connectBlocking();
		
		final TwitterIntegration twitter = new TwitterIntegration(null);
		twitter.retrieveStream();

		api.registerListener(new MessageCreateListener() {
			public void onMessageCreate(DiscordAPI api, Message message) {
				
				twitter.setMessage(message);

				if (message.getContent().contains("rf!music")) {
					message.reply("Soon...");
				} else {
					WordFilter filter = new WordFilter(message);
					filter.checkMessage();
				}
			}
		});
	}
}
