package com.bot.fun;

import java.util.Random;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public abstract class AbstractAction implements Action {
	
	Message message;
	User user;
	
	private static final int HIGH = 100;
	private static final int LOW = 1;
	
	public AbstractAction(Message message, User user) {
		this.message = message;
		this.user = user;
	}
	
	public boolean defy() {
		
		Random r = new Random();
		return r.nextInt(HIGH - LOW) + LOW == 1;
	}
	
	public void reply() {
		if(!defy()) {
			message.reply(complyStatement());
		}
		else {
			message.reply(defyStatement());
		}
	}
}
