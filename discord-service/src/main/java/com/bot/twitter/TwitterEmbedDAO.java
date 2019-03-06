package com.bot.twitter;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.embed.template.TwitterEmbed;

@Component
public class TwitterEmbedDAO {
	
	@Autowired
	private DiscordChannelConnection discordChannel;
	
	public Message postEmbed(TwitterStatus status, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server);
		EmbedBuilder builder = new TwitterEmbed().createEmbed(status);
		return channel.sendMessage(builder).get();
	}
}
