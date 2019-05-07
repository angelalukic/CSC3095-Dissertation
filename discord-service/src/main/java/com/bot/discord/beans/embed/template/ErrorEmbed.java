package com.bot.discord.beans.embed.template;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

@Component
public class ErrorEmbed {
	
	public EmbedBuilder createEmbed(String message) {
		EmbedBuilder builder = new EmbedBuilder();
		builder.setDescription(message)
				.setColor(new Color(255,0,0));
		return builder;
	}
}
