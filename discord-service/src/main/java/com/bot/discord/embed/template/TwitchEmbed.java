package com.bot.discord.embed.template;

import java.awt.Color;
import java.util.Date;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.stereotype.Component;

import com.bot.twitch.beans.TwitchUser;
import com.bot.twitch.beans.events.TwitchChatMessage;
import com.bot.twitch.beans.events.TwitchStreamHost;
import com.bot.twitch.beans.events.TwitchStreamLive;
import com.bot.twitch.beans.events.TwitchStreamOffline;

@Component
public class TwitchEmbed {
	
	public EmbedBuilder createEmbed(TwitchStreamHost event) {
		TwitchUser hostChannel = event.getHostChannel();
		TwitchUser targetChannel = event.getTargetChannel();
		String url = "https://twitch.tv/" + targetChannel.getDisplayName();
		String description = "**" + hostChannel.getDisplayName() + "** has started hosting **" + targetChannel.getDisplayName() + " **"
				+ "playing **" + event.getGame().getName() + "**!\n\nCome and watch here: " + url;
		
		return new EmbedBuilder().setAuthor(targetChannel.getDisplayName(), url, targetChannel.getImage())
				.setDescription(description)
				.setImage(event.getStream().getThumbnailUrl())
				.setThumbnail(event.getGame().getArt())
				.setColor(new Color(100, 65, 164))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createEmbed(TwitchStreamLive event) {
		TwitchUser channel = event.getUser();
		String url = "https://twitch.tv/" + channel.getDisplayName();
		String description = "**" + channel.getDisplayName() + "** has started streaming **" + event.getGame().getName()
				+ "**!\n\nCome and watch here: " + url;
		
		return new EmbedBuilder().setTitle(event.getTitle())
				.setAuthor(channel.getDisplayName(), url, channel.getImage())
				.setDescription(description)
				.setImage(event.getStream().getThumbnailUrl())
				.setThumbnail(event.getGame().getArt())
				.setColor(new Color(100,65,164))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createEmbed(TwitchStreamOffline event) {
		TwitchUser channel = event.getUser();
		String url = "https://twitch.tv/" + channel.getDisplayName();
		String description = "**" + channel.getDisplayName() + "** has stopped streaming. Thank you for tuning in!";
		
		return new EmbedBuilder().setAuthor(channel.getDisplayName(), url, channel.getImage())
				.setDescription(description)
				.setColor(new Color(100,65,164))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createEmbed(TwitchChatMessage message) {
		TwitchUser channel = message.getChannel();
		TwitchUser user = message.getUser();
		String url = "https://twitch.tv/" + channel.getDisplayName();
		
		return new EmbedBuilder().setAuthor(channel.getDisplayName() + ": Chat Log")
		.setUrl(url)
		.setDescription("**" + user.getDisplayName() + "**: " + message.getMessage())
		.setColor(new Color(100, 65, 164))
		.setFooter(new Date().toString());
	}
}
