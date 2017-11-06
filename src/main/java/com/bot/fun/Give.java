package com.bot.fun;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Give extends AbstractAction {
	
	String item;
	
	public static final int LOW = 0;

	public Give(Message message, User user, String item) {
		super(message, user);
		this.item = item;
	}

	public String complyStatement() {
		
		List<String> statements = Arrays.asList(
				"Gives " + user.getMentionTag() + " the " + item + "!",
				"Begrugingly gives " + user.getMentionTag() + " the " + item + "...",
				"Throws the " + item + " at " + user.getMentionTag() + ", looking disgusted."
				);
		
		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}

	public String defyStatement() {
		
		List<String> statements = Arrays.asList(
				"Would a non-robot even need this?",
				"The " + item + " is mine!! *hisses*",
				"Oh? The " + item + "? I already ate it.",
				"Oh! is this for me? Thank you! *snatches it and runs away*"
				);

		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}
}
