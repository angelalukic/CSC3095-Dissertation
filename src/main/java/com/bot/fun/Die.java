package com.bot.fun;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Die extends AbstractAction {
	
	private User user;

	public Die(Message message, User user) {
		super(message);
		this.user = user;
	}

	public String complyStatement() {
		return user.getMentionTag() + " <:bangela:382840385568899072> prepare to fucking die";
	}

	public String defyStatement() {
		return user.getMentionTag() + " <:bangela:382840385568899072> prepare to fucking diw";
	}

}
