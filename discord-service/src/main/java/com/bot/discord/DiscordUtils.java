package com.bot.discord;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.bot.discord.exception.ChannelNotFoundException;

public class DiscordUtils {
	
	public long retrieveWritableChannelId(Server server) {
		List<ServerTextChannel> channels = server.getTextChannels();
		Integer generalChannelIndex = retrieveGeneralChannelIndex(channels);
		
		if(generalChannelIndex != null)
			return channels.get(generalChannelIndex).getId();
	
		// If general channel does not exist or is not writable, return first writable text channel
		for(int i = 0; i < channels.size(); i++) {
			ServerTextChannel channel = channels.get(i);
			if(channel.canYouWrite())
				return channel.getId();
		}
		return 0L;
	}
	
	private Integer retrieveGeneralChannelIndex(List<ServerTextChannel> channels) {
		for(int i = 0; i < channels.size(); i++) {
			ServerTextChannel channel = channels.get(i);
			if(channel.getName().equalsIgnoreCase("general") && channel.canYouWrite())
				return i;
		}
		return null;
	}
	
	public ServerTextChannel retrieveChannelById(Server server, long id) {
		Optional<ServerTextChannel> channel = server.getTextChannelById(id);
		if(channel.isPresent())
			return channel.get();
		return null;		
	}
	
	public User retrieveServerOwnerFromTextChannel(TextChannel channel) {
		Optional<ServerTextChannel> optionalChannel = channel.asServerTextChannel();
		if(optionalChannel.isPresent()) {
			ServerTextChannel textChannel = optionalChannel.get();
			return textChannel.getServer().getOwner();
		}
		throw new ChannelNotFoundException("id="+ channel.getId());
	}
}
