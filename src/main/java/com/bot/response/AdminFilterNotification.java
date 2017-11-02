package com.bot.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class AdminFilterNotification extends AbstractFilterNotification {
	
	private static final String ADMIN_CHANNEL = /*ADMIN CHANNEL HERE*/;
	
	public AdminFilterNotification(Message message, String flaggedWord) {
		super(message, flaggedWord);
	}
	
	public void sendNotification() {
		EmbedBuilder adminEmbed = makeEmbed(getYellow(), "");
		getMessage().getChannelReceiver().getServer().getChannelById(ADMIN_CHANNEL).sendMessage("", adminEmbed);
	}
	
	public void sendCaution() {
		EmbedBuilder adminEmbed = makeEmbed(getOrange(), "");
		getMessage().getChannelReceiver().getServer().getChannelById(ADMIN_CHANNEL).sendMessage("", adminEmbed);
	}
	
	public void sendWarning() {
		EmbedBuilder adminEmbed = makeEmbed(getRed(), "");		
		getMessage().getChannelReceiver().getServer().getChannelById(ADMIN_CHANNEL).sendMessage("", adminEmbed);
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
