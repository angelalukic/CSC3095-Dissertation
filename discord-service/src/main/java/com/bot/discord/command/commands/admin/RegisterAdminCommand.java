package com.bot.discord.command.commands.admin;

import java.util.Optional;

import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RegisterAdminCommand {
	
	@Autowired private DiscordServerRepository repository;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		if(serverRegistered())
			sendServerAlreadyRegisteredErrorMessage();
		else
			registerServer();
	}
	
	private boolean serverRegistered() {
		long id = server.getId();
		Optional<DiscordServer> discordOptional = repository.findById(id);
		return discordOptional.isPresent();
	}
	
	private void registerServer() {
		long serverId = server.getId();
		String name = server.getName();
		long channelId = utils.retrieveWritableChannelId(server);
		DiscordServer discordServer = new DiscordServer(serverId, name, channelId, channelId, channelId, channelId, channelId, "", "#99aab5");
		repository.save(discordServer);
		sendServerRegisteredMessage(channelId);
	}
	
	private void sendServerRegisteredMessage(long channelId) {
		log.info("[" + server.getName() + "] Successfully registered with writable channel ID: " + channelId);
		EmbedBuilder embed = successEmbed.createEmbed("Server has been successfully registered. Default notification channels"
				+ " have all been set to <#" + channelId + ">. If you wish to change this please use command `rf@notification help`.");
		utils.sendMessage(embed, event);
	}
	
	private void sendServerAlreadyRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Register Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed("**Error**: Server has already been regsistered.");
		utils.sendMessage(embed, event);
	}
}
