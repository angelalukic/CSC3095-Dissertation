package com.bot.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public interface Notification {
	
	public void sendCaution();
	
	public void sendWarning();
	
	public EmbedBuilder makeEmbed(Color color, String text);
}
