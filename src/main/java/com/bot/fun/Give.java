package com.bot.fun;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Give extends AbstractAction {
	
	private String item;
	private User user;
	
	private static final int LOW = 0;

	public Give(Message message, User user, String item) {
		super(message);
		this.user = user;
		this.item = item;
	}

	public String complyStatement() {
		
		List<String> statements = Arrays.asList(
				"Gives " + user.getMentionTag() + " " + item + "!",
				"Begrugingly gives " + user.getMentionTag() + " " + item + "...",
				"Throws " + item + " at " + user.getMentionTag() + ", looking disgusted."
				);
		
		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}

	public String defyStatement() {
		
		List<String> statements = Arrays.asList(
				"Would a non-robot even need this?",
				item + " is mine!! *hisses*",
				"Oh? " + item + "? I already ate it.",
				"Oh! is this for me? Thank you! *snatches it and runs away*"
				);

		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}
}
