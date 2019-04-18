package com.bot.discord.beans.embed.template;

import java.awt.Color;

import org.javacord.api.entity.message.embed.EmbedBuilder;

import com.bot.twitter.beans.TwitterStatus;
import com.bot.twitter.beans.TwitterUser;

public class TwitterEmbed {
	
	public EmbedBuilder createEmbed(TwitterStatus status) {
		
		EmbedBuilder builder = new EmbedBuilder();
		
		TwitterUser user = status.getUser();
		String url = "https://twitter.com/" + user.getScreenName() + "/status/" + status.getId();
		
		builder.setAuthor(user.getName(), url, user.getProfilePicture())
				.setDescription(status.getBody())
				.setColor(new Color(29,161,242))
				.setFooter(status.getDate().toString());
		
		if(status.getVideo() != null)
		    builder.setImage(status.getVideo());
		
		else if(status.getImage() != null)
			builder.setImage(status.getImage());
		
		return builder;
	}
}
