package com.bot.twitch.response;

import de.btobastian.javacord.entities.message.embed.EmbedBuilder;

public interface TwitchNotification {
	
	public void sendNotification();
	
	public EmbedBuilder makeEmbed();
}
