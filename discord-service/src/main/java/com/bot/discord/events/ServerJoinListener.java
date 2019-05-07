package com.bot.discord.events;

import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.discord.beans.server.DiscordServerRepository;


@Component
public class ServerJoinListener {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordUtils utils;
	private ServerJoinEvent event;
	private EmbedBuilder embed;
	private ServerTextChannel channel;
	
	public void execute(ServerJoinEvent event) {
		this.event = event;
		embed = serverJoinEmbed();
		long channelId = utils.retrieveWritableChannelId(event.getServer());

		// If there is a writable channel within the server then send a message to that channel
		if (channelId != 0L) {
			channel = utils.retrieveChannelById(event.getServer(), channelId);
			sendMessageToWritableChannel();
			sendServerConfigurationMessage(channelId);
		}
		
		// Send a message to the server owner
		sendMessageToServerOwner();
	}

	private void sendMessageToServerOwner() {
		User serverOwner = event.getServer().getOwner();
		serverOwner.sendMessage(embed);
	}
	
	private void sendMessageToWritableChannel() {
		if(channel != null) {
			channel.sendMessage(embed);
		}
	}
	
	private void sendServerConfigurationMessage(long channelId) {
		long id = event.getServer().getId();
		String serverName = event.getServer().getName();
		Optional<DiscordServer> discordOptional = repository.findById(id);
		
		if(discordOptional.isPresent())
			sendDiscordServerConfigurationExists();
		else {
			DiscordServer server = new DiscordServer(id, serverName, channelId, channelId, channelId, channelId, channelId, "", "#99aab5", false, channelId);
			repository.save(server);
			sendDiscordServerConfigurationCreated();
		}
	}
	
	private void sendDiscordServerConfigurationExists() {
		embed.setTitle(channel.getServer().getName())
				.setDescription("Server is already registered. Settings have been automatically loaded.");
		sendMessageToServerOwner();
		sendMessageToWritableChannel();
	}
	
	private void sendDiscordServerConfigurationCreated() {
		embed.setTitle(channel.getServer().getName())
				.setDescription("New server detected. Server has been automatically registered.");
		sendMessageToServerOwner();
		sendMessageToWritableChannel();
	}

	private EmbedBuilder serverJoinEmbed() {
		return new EmbedBuilder().setDescription("Hi! My name is RF! I am a bot designed to help with **automatically moderating** and **managing social media accounts**! "
				+ "I have just been added to your server \"" + event.getServer().getName() + "\"!")
				.addField("Permissions", "By default I am not set to be an administrator. Please make sure to either set me to be an administrator"
						+ "or manually add me to the channels you want me to be able to post in!")
				.addField("Admin Channel", "To begin, please set a channel for me to post important messages to. "
						+ "I recommend this be a channel only administrators can see! Use command:\n`rf@notification admin <channel>`")
				.addField("Invite Link", "If you want to invite me to more servers, please use the following link: "
						+ "<https://discordapp.com/oauth2/authorize?client_id=374850903649419264&scope=bot&permissions=126016>")
				.setFooter("If you encounter any issues, be sure to send a message to Angeljho#2514 on Discord!");
	}
}
