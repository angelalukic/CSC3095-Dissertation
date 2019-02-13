package com.bot.discord.embed.twitter;

import java.awt.Color;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.Configuration;
import com.bot.discord.exception.ChannelNotFoundException;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;
import com.bot.twitter.TwitterStatus;
import com.bot.twitter.TwitterUser;

@Component
public class TwitterEmbedDAO {
	
	@Autowired
	private Configuration configuration;
	
	@Autowired
	private DiscordServerRepository serverRepository;
	
	public Message postEmbed(TwitterStatus status, long server) throws InterruptedException, ExecutionException {
		DiscordApi api = connect();
		EmbedBuilder builder = createEmbed(status);
		
		Optional<DiscordServer> optionalServer = serverRepository.findById(server);
		
		if(!optionalServer.isPresent())
			throw new ServerNotFoundException("id- " + server);
		
		DiscordServer discordServer = optionalServer.get();
		long id = discordServer.getNotificationChannel();
		
		Optional<TextChannel> optionalChannel = api.getTextChannelById(id);
		
		if(!optionalChannel.isPresent())
			throw new ChannelNotFoundException("id-" + id);
		
		return optionalChannel.get().sendMessage(builder).get();
	}
	
	private DiscordApi connect() {
		String token = configuration.getToken();
		return new DiscordApiBuilder().setToken(token).login().join();
	}
	
	private EmbedBuilder createEmbed(TwitterStatus status) {
		
		TwitterUser user = status.getUser();
		EmbedBuilder builder = new EmbedBuilder();
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
