package com.bot.discord.embed.twitter;

import java.awt.Color;
import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.twitter.TwitterStatus;
import com.bot.twitter.TwitterUser;

@Component
public class TwitterEmbedDAO {
	
	@Autowired
	private Configuration configuration;
	
	public Message postEmbed(TwitterStatus status, long server) throws InterruptedException, ExecutionException {
		DiscordApi api = connect();
		EmbedBuilder builder = createEmbed(status);
		
		long id = 375672676859248660L; // need to retrieve channel ID from database
		
		return api.getTextChannelById(id).get().sendMessage(builder).get();
	}
	
	private DiscordApi connect() {
		String token = configuration.getToken();
		return new DiscordApiBuilder().setToken(token).login().join();
	}
	
	private EmbedBuilder createEmbed(TwitterStatus status) {
		
		TwitterUser user = status.getUser();
		
		return new EmbedBuilder()
				.setAuthor(user.getName(), user.getUrl(), user.getProfilePicture())
				.setDescription(status.getBody())
				.setColor(new Color(29,161,242))
				.setImage(user.getProfileBanner())
				.setTitle(user.getScreenName())
				.setFooter(status.getDate().toString());
	}
}
