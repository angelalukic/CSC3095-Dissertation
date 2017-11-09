package com.bot.fun;

import java.util.Random;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import lombok.Getter;

@Getter
public abstract class AbstractAction implements Action {
	
	private Message message;
	
	private static final int HIGH = 100;
	private static final int LOW = 1;
	
	public AbstractAction(Message message) {
		this.message = message;
	}
	
	public boolean defy() {
		
		Random r = new Random();
		return r.nextInt(HIGH - LOW) + LOW == 1;
	}
	
	public void reply() {
		if(!defy()) {
			getMessage().reply(complyStatement());
		}
		else {
			getMessage().reply(defyStatement());
		}
	}
}
