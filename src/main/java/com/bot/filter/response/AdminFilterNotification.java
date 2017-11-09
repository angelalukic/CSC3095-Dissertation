package com.bot.filter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public class AdminFilterNotification extends AbstractFilterNotification {
	
	private Channel channel;
	private Channel botReportsChannel;
	
	private static final String BOT_REPORTS_CHANNEL = "";
	
	public AdminFilterNotification(Message message, String flaggedWord) {
		super(message, flaggedWord);
		this.channel = message.getChannelReceiver();
		this.botReportsChannel = channel.getServer().getChannelById(BOT_REPORTS_CHANNEL);
	}
	
	public void sendNotification() {
		EmbedBuilder embed = makeEmbed(getYellow(), "");
		botReportsChannel.sendMessage("", embed);
	}
	
	public void sendCaution() {
		EmbedBuilder embed = makeEmbed(getOrange(), "");
		botReportsChannel.sendMessage("", embed);
	}
	
	public void sendWarning() {	
		EmbedBuilder embed = makeEmbed(getRed(), "");
		botReportsChannel.sendMessage("", embed);
	}
	
	public EmbedBuilder makeEmbed(Color color, String text) {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription( getMessage().getAuthor().getMentionTag()
				+ " caught saying \"" + getFlaggedWord() + "\" in channel "
				+ channel.getMentionTag());
		output.addField("Orignal Message", getMessage().getContent(), true);
		
		return output;
	}
}
