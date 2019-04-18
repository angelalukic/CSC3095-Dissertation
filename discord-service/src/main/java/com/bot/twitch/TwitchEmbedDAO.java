package com.bot.twitch;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.beans.embed.template.TwitchEmbed;
import com.bot.twitch.beans.events.TwitchChatMessage;
import com.bot.twitch.beans.events.TwitchStreamHost;
import com.bot.twitch.beans.events.TwitchStreamLive;
import com.bot.twitch.beans.events.TwitchStreamOffline;

@Component
public class TwitchEmbedDAO {
	
	private static final String NOTIFICATION = "notification";
	private static final String TWITCH_LOGS = "twitchlogs";
	
	@Autowired private DiscordChannelConnection discordChannel;
	@Autowired private TwitchEmbed embed;
	
	public Message postEmbed(TwitchStreamHost event, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, NOTIFICATION);
		EmbedBuilder builder = embed.createEmbed(event);
		return channel.sendMessage(builder).get();
	}
	
	public Message postEmbed(TwitchStreamLive event, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, NOTIFICATION);
		EmbedBuilder builder = embed.createEmbed(event);
		return channel.sendMessage(builder).get();
	}
	
	public Message postEmbed(TwitchStreamOffline event, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, NOTIFICATION);
		EmbedBuilder builder = embed.createEmbed(event);
		return channel.sendMessage(builder).get();
	}
	
	public Message postEmbed(TwitchChatMessage event, long server) throws InterruptedException, ExecutionException {
		TextChannel channel = discordChannel.connect(server, TWITCH_LOGS);
		EmbedBuilder builder = embed.createEmbed(event);
		return channel.sendMessage(builder).get();
	}
}
