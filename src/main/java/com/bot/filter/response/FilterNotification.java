package com.bot.filter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public interface FilterNotification {
	
	public void sendNotification();
	
	public void sendCaution();
	
	public void sendWarning();
	
	public EmbedBuilder makeEmbed(Color color, String text);
}
