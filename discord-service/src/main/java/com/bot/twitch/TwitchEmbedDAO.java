package com.bot.twitch;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.embed.template.TwitchEmbed;
import com.bot.twitch.events.TwitchChatMessage;
import com.bot.twitch.events.TwitchStreamHost;

@Component
public class TwitchEmbedDAO {
	
	private static final String NOTIFICATION = "notification";
	private static final String TWITCH_LOGS = "twitchlogs";
	
	@Autowired
	private DiscordChannelConnection discordChannel;
	
	public Message postEmbed(TwitchStreamHost status, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, NOTIFICATION);
		EmbedBuilder builder = new TwitchEmbed().createEmbed(status);
		return channel.sendMessage(builder).get();
	}
	
	public Message postEmbed(TwitchChatMessage message, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, TWITCH_LOGS);
		EmbedBuilder builder = new TwitchEmbed().createEmbed(message);
		return channel.sendMessage(builder).get();
	}
}
