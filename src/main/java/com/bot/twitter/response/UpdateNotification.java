package com.bot.twitter.response;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public interface UpdateNotification {
	
	public void sendNotification();
	
	public EmbedBuilder makeEmbed();
}
