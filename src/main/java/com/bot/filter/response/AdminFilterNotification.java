package com.bot.filter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class AdminFilterNotification extends AbstractFilterNotification {
	
	private static final String BOT_REPORTS_CHANNEL = "";
	
	public AdminFilterNotification(Message message, String flaggedWord) {
		super(message, flaggedWord);
	}
	
	public void sendNotification() {
		getMessage().getChannelReceiver().getServer().getChannelById(BOT_REPORTS_CHANNEL)
			.sendMessage("", makeEmbed(getYellow(), ""));
	}
	
	public void sendCaution() {
		getMessage().getChannelReceiver().getServer().getChannelById(BOT_REPORTS_CHANNEL)
			.sendMessage("", makeEmbed(getOrange(), ""));
	}
	
	public void sendWarning() {	
		getMessage().getChannelReceiver().getServer().getChannelById(BOT_REPORTS_CHANNEL)
			.sendMessage("", makeEmbed(getRed(), ""));
	}
	
	public EmbedBuilder makeEmbed(Color color, String text) {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription( getMessage().getAuthor().getMentionTag()
				+ " caught saying \"" + getFlaggedWord() + "\" in channel "
				+ getMessage().getChannelReceiver().getMentionTag());
		output.addField("Orignal Message", getMessage().getContent(), true);
		
		return output;
	}
}
