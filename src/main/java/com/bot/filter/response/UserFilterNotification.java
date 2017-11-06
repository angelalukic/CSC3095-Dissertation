package com.bot.filter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class UserFilterNotification extends AbstractFilterNotification {
	
	private static final String WELCOME_CHANNEL = "";
	
	public UserFilterNotification(Message message, String flaggedWord) {
		super(message, flaggedWord);
	}
	
	public void sendCaution() {
		EmbedBuilder userEmbed = makeEmbed(getOrange(),
				"This is a word identified to potentially cause offense if used out of context, "
				+ "or deemed to be of potential political/historical context which is currently discouraged. "
				+ "If you have used the word within a sensible context please ignore this message.");
		getMessage().getAuthor().sendMessage("", userEmbed);
	}
	
	public void sendWarning() {
		EmbedBuilder userEmbed = makeEmbed(getRed(), 
				"As per university policy we do not tolerate the use of slurs within our channel.");		
		getMessage().getAuthor().sendMessage("", userEmbed);
	}
	
	public EmbedBuilder makeEmbed(Color color, String text) {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription( "You have been caught saying \"" + getFlaggedWord() + 
				"\" in channel " + getMessage().getChannelReceiver().getMentionTag()
				+ ". " + text + " Please remind yourself of the rules detailed in " 
				+ getMessage().getChannelReceiver().getServer().getChannelById(WELCOME_CHANNEL).getMentionTag()
				+ ". The administrators have been notified.");
		output.addField("Orignal Message", getMessage().getContent(), true);
		
		return output;
	}
}
