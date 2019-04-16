package com.bot.discord.events;

import java.util.Optional;

import org.javacord.api.entity.channel.ServerTextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.server.member.ServerMemberJoinEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ServerMemberJoinListener {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	private ServerMemberJoinEvent event;
	private Server server;
	
	public void execute(ServerMemberJoinEvent event) {
		this.event = event;
		server = event.getServer();
		long id = event.getServer().getId();
		Optional<DiscordServer> serverOptional = repository.findById(id);
		if(serverOptional.isPresent()) {
			DiscordServer discordServer = serverOptional.get();
			if(!discordServer.getServerJoinMessage().isEmpty())
				sendMessage(discordServer);
		}
	}

	private void sendMessage(DiscordServer discordServer) {
		ServerTextChannel channel = utils.retrieveChannelById(event.getServer(), discordServer.getServerJoinChannel());
		if(channel != null)
			sendWelcomeMessage(discordServer, channel);
		else 
			sendErrorMessageToServerOwner(discordServer);
	}
	
	private void sendWelcomeMessage(DiscordServer discordServer, ServerTextChannel channel) {
		log.info("[" + server.getName() + "] Sending Welcome Message in Channel " + channel.getName());
		String welcome = discordServer.getServerJoinMessage()
				.replace("[USER]", "<@" + event.getUser().getId() + ">")
				.replace("[SERVER]", "**" + server.getName() + "**")
				.replace("[SERVEROWNER]", "<@" + server.getOwner().getId() + ">");
		EmbedBuilder embed = new EmbedBuilder().setDescription(welcome);
		utils.sendMessage(embed, event, channel);
	}
	
	private void sendErrorMessageToServerOwner(DiscordServer discordServer) {
		log.error("[" + server.getName() + "] Error when Sending Welcome Message: Channel Doesn't Exist, Server Owner Notified");
		User serverOwner = event.getServer().getOwner();
		EmbedBuilder embed = errorEmbed.createEmbed("Attempted to send a welcome message in channel with ID " + discordServer.getServerJoinChannel() + " in server **"
				+ discordServer.getName() + "** but the channel was not found. Please fix this with command rf@notification welcome <channel>`.");
		serverOwner.sendMessage(embed);
	}
}
