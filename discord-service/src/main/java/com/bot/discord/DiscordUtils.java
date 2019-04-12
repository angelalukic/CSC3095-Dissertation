package com.bot.discord;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.exception.ChannelNotFoundException;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.exception.UserNotFoundException;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

@Component
public class DiscordUtils {
	
	@Autowired private DiscordServerRepository discordServerRepository;
	
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
	
	///////////////////////
	
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
	
	public DiscordServer getDiscordServerFromServerOptional(Optional<Server> serverOptional, long id) {
		Server server = getServerFromServerOptional(serverOptional, id);
		long serverId = server.getId();
		Optional<DiscordServer> discordOptional = discordServerRepository.findById(serverId);
		if(discordOptional.isPresent())
			return discordOptional.get();
		else
			throw new ServerNotFoundException("id=" + id);
	}
	
	public Server getServerFromServerOptional(Optional<Server> server, long id) {
		if(server.isPresent())
			return server.get();
		throw new ServerNotFoundException("id=" + id);
	}
	
	public User getUserFromUserOptional(Optional<User> user, long id) {
		if(user.isPresent())
			return user.get();
		throw new UserNotFoundException("id=" + id);
	}
	
	public void sendMessage(EmbedBuilder embed, MessageCreateEvent event) {
		Message message = event.getMessage();
		if(message.getChannel().canYouWrite())
			message.getChannel().sendMessage(embed);
		else {
			User user = getUserFromUserOptional(message.getUserAuthor(), message.getId());
			user.sendMessage(embed);
		}
	}
}
