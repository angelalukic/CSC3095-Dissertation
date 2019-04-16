package com.bot.twitter;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.TwitterEmbed;

@Component
public class TwitterEmbedDAO {
	
	private static final String NOTIFICATION = "notification";
	
	@Autowired
	private DiscordChannelConnection discordChannel;
	
	public Message postEmbed(TwitterStatus status, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, NOTIFICATION);
		EmbedBuilder builder = new TwitterEmbed().createEmbed(status);
		if(channel.getId() != 0L && channel.canYouWrite())
			return channel.sendMessage(builder).get();
		else
			return sendMessageToServerOwner(channel, builder);
	}
	
	private Message sendMessageToServerOwner(TextChannel channel, EmbedBuilder builder)	throws InterruptedException, ExecutionException {
		DiscordUtils utils = new DiscordUtils();
		User serverOwner = utils.retrieveServerOwnerFromTextChannel(channel);
		return serverOwner.sendMessage(builder).get();
	}
}