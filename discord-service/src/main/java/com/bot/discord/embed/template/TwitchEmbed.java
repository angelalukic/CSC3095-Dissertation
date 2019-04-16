package com.bot.discord.embed.template;

import java.awt.Color;
import java.util.Date;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.bot.twitch.TwitchUser;
import com.bot.twitch.events.TwitchChatMessage;
import com.bot.twitch.events.TwitchStreamHost;

public class TwitchEmbed {
	
	public EmbedBuilder createEmbed(TwitchStreamHost event) {
		
		EmbedBuilder builder = new EmbedBuilder();
	
		TwitchUser hostChannel = event.getHostChannel();
		TwitchUser targetChannel = event.getTargetChannel();
		String url = "https://twitch.tv/" + targetChannel.getDisplayName();
		String description = "**" + hostChannel.getDisplayName() + "** has started hosting **" + targetChannel.getDisplayName() + " **"
				+ "playing **" + event.getGame().getName() + "**!\n\nCome and watch here: " + url;
		
		builder.setAuthor(targetChannel.getDisplayName(), url, targetChannel.getImage())
				.setDescription(description)
				.setImage(event.getStream().getThumbnailUrl())
				.setThumbnail(event.getGame().getArt())
				.setColor(new Color(100, 65, 164))
				.setFooter(new Date().toString());
		
		return builder;
	}
	
	public EmbedBuilder createEmbed(TwitchChatMessage message) {
		
		EmbedBuilder builder = new EmbedBuilder();
		
		TwitchUser channel = message.getChannel();
		TwitchUser user = message.getUser();
		String url = "https://twitch.tv/" + channel.getDisplayName();
		
		builder.setAuthor(channel.getDisplayName() + ": Chat Log")
		.setUrl(url)
		.setDescription("**" + user.getDisplayName() + "**: " + message.getMessage())
		.setColor(new Color(100, 65, 164))
		.setFooter(new Date().toString());
		
		return builder;
	}
}
