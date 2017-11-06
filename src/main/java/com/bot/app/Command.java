package com.bot.app;

import com.bot.filter.WordFilter;
import com.bot.fun.Action;
import com.bot.fun.Give;
import com.bot.fun.Hug;

import de.btobastian.javacord.entities.message.Message;

public class Command {
	
	Message message;
	
	public Command(Message message) {
		this.message = message;
	}
	
	public void execute() {
		
		WordFilter filter = new WordFilter(message);
		filter.checkMessage();
		
		if(message.getContent().substring(0,3).equalsIgnoreCase("rf!")) {
			String input = message.getContent().replace("rf!", "");
			determineCommand(splitArguments(input));
		}		
	}
	
	private String[] splitArguments(String input) {
		return input.split(" ");
	}
	
	private void determineCommand(String[] args) {
		
		if (args[0].equalsIgnoreCase("music")) {
			message.reply("Soon...");
		} 
		
		else if(args[0].equalsIgnoreCase("give") || args[0].equalsIgnoreCase("hug")) {
			performAction(args);
		}
	}
	
	private void performAction(String[] args) {
		
		Action action;
		
		if(args[0].equalsIgnoreCase("give")) {
			
			StringBuilder item = new StringBuilder();
			for(int i = 2; i < args.length; i++) {
				item.append(args[i] + " ");
			}
			
			try {
				action = new Give(message, message.getMentions().get(0), item.toString());
				action.reply();
			} catch (IndexOutOfBoundsException e) {
				message.reply("I don't know who to give this item to..");
			}
		} else {
			try {
				action = new Hug(message, message.getMentions().get(0));
				action.reply();
			} catch (IndexOutOfBoundsException e) {
				message.reply("You didn't tell me who to hug..");
			}
		}
	}
}
