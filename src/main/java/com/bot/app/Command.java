package com.bot.app;

import java.util.Random;

import com.bot.filter.WordFilter;
import com.bot.fun.Action;
import com.bot.fun.Die;
import com.bot.fun.Give;
import com.bot.fun.Hug;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Command {
	
	private Message message;
	
	public Command(Message message) {
		this.message = message;
	}
	
	public void execute() {
		
		WordFilter filter = new WordFilter(message);
		filter.checkMessage();
		
		String messageContent = message.getContent();
		
		if(messageContent.length() > 2 && 
				messageContent.substring(0,3).equalsIgnoreCase("rf!")) {
			String query = messageContent.replace("rf!", "");
			determineCommand(splitArguments(query));
		}		
	}
	
	private String[] splitArguments(String input) {
		return input.split(" ");
	}
	
	private void determineCommand(String[] args) {
		
		String command = args[0]; 
		
		if(command.equalsIgnoreCase("cactuspie")) {
			
			Random random = new Random();
			int max = 3999;
			int min = 0;
			
			int scp = random.nextInt((max - min) + 1) + min;
			message.reply("http://www.scp-wiki.net/scp-" +  String.format("%03d", scp));
		} else if(command.equalsIgnoreCase("youtube")) {
			message.reply("https://www.youtube.com/channel/UC7PeKnuBCx5MDPj_f1V90sQ");
		} else if(command.equalsIgnoreCase("facebook")) {
			message.reply("https://www.facebook.com/groups/nugamesoc");
		} else if(command.equalsIgnoreCase("twitch")) {
			message.reply("https://www.twitch.tv/nclgamingsociety");
		} else if(command.equalsIgnoreCase("twitter")) {
			message.reply("https://twitter.com/NewcastleGaming");
		} else if(command.equalsIgnoreCase("nusu")) {
			message.reply("https://www.nusu.co.uk/getinvolved/societies/society/GamingSoc/");
		} else if(command.equalsIgnoreCase("nuel")) {
			message.reply("https://thenuel.com/university/3244");
		} else if(command.equalsIgnoreCase("battlenet")) {
			message.reply("https://blizzard.com/invite/P7jEjiXd");
		} else {
			performAction(args);
		}
	}
	
	private void performAction(String[] args) {
		
		Action action;
		String command = args[0];
		
		try {
			User user = message.getMentions().get(0);
			
			if(command.equalsIgnoreCase("give")) {
				
				StringBuilder item = new StringBuilder();
				for(int i = 2; i < args.length; i++) {
					item.append(args[i] + " ");
				}
				
				action = new Give(message, user, item.toString().trim());
				action.reply();
			} else if(command.equalsIgnoreCase("die")) {
				action = new Die(message, user);
				action.reply();
			} else if (command.equalsIgnoreCase("hug")) {
				action = new Hug(message, user);
				action.reply();
			} else {
				message.reply("I don't know how to do that, sorry :[");
			}
		} catch (IndexOutOfBoundsException e) {
			
			if(command.equalsIgnoreCase("die")) {
				message.reply("<:bangela:382840385568899072> prepare to fucking die");
			} else {
				message.reply("I don't know how to do that, sorry :[");
			}
		}
	}
}
