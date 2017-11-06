package com.bot.fun;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;

public class Hug extends AbstractAction {
	
	public static final int LOW = 0;

	public Hug(Message message, User user) {
		super(message, user);
	}
	
	public String complyStatement() {
		List<String> statements = Arrays.asList(
				"Big hugs for " + user.getMentionTag() + "!!",
				"Gives " + user.getMentionTag() + " a big cuddle!",
				"It's a three way hug between " + message.getAuthor().getMentionTag() + ", " + user.getMentionTag() + " and me! Yay!",
				"Snuggles down with " + user.getMentionTag() + " <3"
				);
		
		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}
	
	public String defyStatement() {
		List<String> statements = Arrays.asList(
				"I don't want to hug " + user.getMentionTag() + "!!",
				user.getMentionTag() + " smells like they haven't showered for 5 days...",
				"I'm all hugged out...", 
				"Can't hug somebody that isn't metallic.",
				"I... I -ddon't take orders f-from you... b-b-baka!!",
				"*stares at " + user.getMentionTag() + "'s butt instead."
				);
		
		Random r = new Random();
		return statements.get(r.nextInt(statements.size() - LOW) + LOW);
	}
}
