package com.bot.app;

import com.bot.filter.WordFilter;
import com.bot.fun.Action;
import com.bot.fun.Give;
import com.bot.fun.Hug;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Command {
	
	Message message;
	
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
		
		if (command.equalsIgnoreCase("music")) {
			message.reply("Soon...");
		} 
		
		else if(command.equalsIgnoreCase("give") || command.equalsIgnoreCase("hug")) {
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
			} else {
				action = new Hug(message, user);
				action.reply();
			}
		} catch (IndexOutOfBoundsException e) {
			message.reply("I cannot do this if you do not give me a user!");
		}
	}
}
