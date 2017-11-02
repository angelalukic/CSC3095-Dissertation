package com.bot.app;

import com.bot.filter.WordFilter;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

public class BotApp {

	public static void main(String[] args) {

		DiscordAPI api = Javacord.getApi(/* KEY HERE */, true);
		api.connectBlocking();

		api.registerListener(new MessageCreateListener() {
			public void onMessageCreate(DiscordAPI api, Message message) {

				if (message.getContent().length() > 3 
						&& message.getContent().substring(0,3).contains("rf!")) {
					message.reply("What is your command?");
				}
				else {
					WordFilter filter = new WordFilter(message);
					filter.checkMessage();
				}
			}
		});
	}
}
