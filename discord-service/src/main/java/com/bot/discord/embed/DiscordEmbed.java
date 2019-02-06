package com.bot.discord.embed;

import java.awt.Color;
import java.net.URL;
import java.util.List;

import org.javacord.api.entity.message.embed.Embed;
import org.javacord.api.entity.message.embed.EmbedAuthor;
import org.javacord.api.entity.message.embed.EmbedField;
import org.javacord.api.entity.message.embed.EmbedFooter;
import org.javacord.api.entity.message.embed.EmbedImage;
import org.javacord.api.entity.message.embed.EmbedThumbnail;
import org.javacord.api.entity.message.embed.EmbedVideo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DiscordEmbed {
	
	private String authorName;
	private String authorURL;
	private String authorIcon;
	private Color colour;
	private String description;
	private String footerText;
	private String image;
	private String thumbnail;
	private String title;
	
	public DiscordEmbed() {
	}

	// Lots of parameters permitted as we are dealing with JSON
	public DiscordEmbed(String authorName, String authorURL, String authorIcon, Color colour, String description,
			String footerText, String image, String thumbnail, String title) {
		
		this.authorName = authorName;
		this.authorURL = authorURL;
		this.authorIcon = authorIcon;
		this.colour = colour;
		this.description = description;
		this.footerText = footerText;
		this.image = image;
		this.thumbnail = thumbnail;
		this.title = title;
	}
}