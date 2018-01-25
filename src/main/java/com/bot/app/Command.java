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
			message.reply("http://www.scp-wiki.net/scp-" + scp);
		}
		
		else {
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
