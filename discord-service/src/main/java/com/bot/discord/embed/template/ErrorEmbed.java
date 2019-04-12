package com.bot.discord.embed.template;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;

public class ErrorEmbed {
	
	public EmbedBuilder createEmbed(String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message)
				.setColor(new Color(255,0,0));
		return builder;
	}
}
