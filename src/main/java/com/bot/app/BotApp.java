package com.bot.app;

import com.bot.filter.WordFilter;
import com.bot.fun.Action;
import com.bot.fun.Give;
import com.bot.fun.Hug;
import com.bot.twitter.TwitterIntegration;

import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.Javacord;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;

public class BotApp {

	public static void main(String[] args) {

		DiscordAPI api = Javacord.getApi("", true);
		api.connectBlocking();
		
		final TwitterIntegration twitter = new TwitterIntegration(null);
		twitter.retrieveStream();

		api.registerListener(new MessageCreateListener() {
			public void onMessageCreate(DiscordAPI api, Message message) {
				
				twitter.setMessage(message);

				if (message.getContent().toLowerCase().contains("rf!music")) {
					message.reply("Soon...");
				} 
				
				else if(message.getContent().toLowerCase().contains("rf!give")) {
					try {
						String[] args = message.getContent().split(" ");
						Action give = new Give(message, message.getMentions().get(0), args[1]);
						give.reply();
					} catch(IndexOutOfBoundsException e) {
						message.reply("You didn't give me someone to give this item to..");
					}
				}
				
				else if(message.getContent().toLowerCase().contains("rf!hug")) {
					try {
						Action hug = new Hug(message, message.getMentions().get(0));
						hug.reply();
					} catch(IndexOutOfBoundsException e) {
						message.reply("You didn't give me someone to hug..");
					}
				}
				
				
				else {
					WordFilter filter = new WordFilter(message);
					filter.checkMessage();
				}
			}
		});
	}
}
