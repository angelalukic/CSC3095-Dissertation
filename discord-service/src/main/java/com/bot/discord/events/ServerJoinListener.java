package com.bot.discord.events;

import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.ServerJoinEvent;

import com.bot.discord.DiscordUtils;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

public class ServerJoinListener {
	
	private ServerJoinEvent event;
	private DiscordServerRepository repository;
	
	public ServerJoinListener(ServerJoinEvent event, DiscordServerRepository repository) {
		this.event = event;
		this.repository = repository;
	}
	
	public void execute() {
		EmbedBuilder embed = retrieveJoinMessage();
		DiscordUtils utils = new DiscordUtils();
		long channelId = utils.retrieveWritableChannelId(event.getServer());

		if (channelId != 0L) {
			ServerTextChannel channel = utils.retrieveChannelById(event.getServer(), channelId);
			sendMessageToWritableChannel(channel, embed);
			sendServerConfigurationMessage(channel, channelId);
		}
		sendMessageToServerOwner(embed);
	}

	private void sendMessageToServerOwner(EmbedBuilder embed) {
		User serverOwner = event.getServer().getOwner();
		serverOwner.sendMessage(embed);
	}
	
	private void sendMessageToWritableChannel(ServerTextChannel channel, EmbedBuilder embed) {
		if(channel != null) {
			channel.sendMessage(embed);
		}
	}
	
	private void sendServerConfigurationMessage(ServerTextChannel channel, long channelId) {
		long id = event.getServer().getId();
		String serverName = event.getServer().getName();
		Optional<DiscordServer> discordOptional = repository.findById(id);
		
		if(discordOptional.isPresent())
			sendDiscordServerConfigurationExists(channel);
		else {
			DiscordServer server = new DiscordServer(id, serverName, channelId, channelId, channelId, channelId);
			server = repository.save(server);
			sendDiscordServerConfigurationCreated(channel);
		}
	}
	
	private void sendDiscordServerConfigurationExists(ServerTextChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setTitle(channel.getServer().getName())
				.setDescription("Server is already registered. Settings have been automatically loaded.");
		
		sendMessageToServerOwner(embed);
		sendMessageToWritableChannel(channel, embed);
	}
	
	private void sendDiscordServerConfigurationCreated(ServerTextChannel channel) {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setTitle(channel.getServer().getName())
				.setDescription("New server detected. Server has been automatically registered.");
		
		sendMessageToServerOwner( embed);
		sendMessageToWritableChannel(channel, embed);
	}

	private EmbedBuilder retrieveJoinMessage() {
		EmbedBuilder embed = new EmbedBuilder();
		
		embed.setDescription("Hi! My name is RF! I am a bot designed to help with **automatically moderating** and **managing social media accounts**! "
				+ "I have just been added to your server \"" + event.getServer().getName() + "\"!")
				.addField("Permissions", "By default I am not set to be an administrator. Please make sure to either set me to be an administrator"
						+ "or manually add me to the channels you want me to be able to post in!")
				.addField("Admin Channel", "To begin, please set a channel for me to post important messages to. "
						+ "I recommend this be a channel only administrators can see! Use command:\n`rf@notification admin <channel>`")
				.addField("Invite Link", "If you want to invite me to more servers, please use the following link: "
						+ "<https://discordapp.com/oauth2/authorize?client_id=374850903649419264&scope=bot&permissions=126016>")
				.setFooter("If you encounter any issues, be sure to send a message to Angeljho#2514 on Discord!");
		
		return embed;
	}
}
