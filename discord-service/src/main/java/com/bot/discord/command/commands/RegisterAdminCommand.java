package com.bot.discord.command.commands;

import java.util.Optional;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.server.DiscordServer;
import com.bot.discord.server.DiscordServerRepository;

public class RegisterAdminCommand {
	
	private Message message;
	private DiscordServerRepository repository;
	
	public RegisterAdminCommand(Message message, DiscordServerRepository repository) {
		this.message = message;
		this.repository = repository;
	}
	
	public void execute() {
		Optional<Server> serverOptional = message.getServer();
		
		if(serverOptional.isPresent()) {
			Server server = serverOptional.get();
			if(serverRegistered(server))
				sendErrorMessage("Server has already been registered.");
			else
				registerServer(server);
		}
		
		else
			sendErrorMessage("Unknown error has occured, server has not been registered.");
	}
	
	private boolean serverRegistered(Server server) {
		long id = server.getId();
		Optional<DiscordServer> discordOptional = repository.findById(id);
		return discordOptional.isPresent();
	}
	
	private void registerServer(Server server) {
		long serverId = server.getId();
		String name = server.getName();
		DiscordUtils utils = new DiscordUtils();
		long channelId = utils.retrieveWritableChannelId(server);
		DiscordServer discordServer = new DiscordServer(serverId, name, channelId, channelId, channelId, channelId);
		repository.save(discordServer);
		sendSuccessMessage("Server has successfully been registered.");
	}
	
	private void sendSuccessMessage(String description) {
		EmbedBuilder embed = new SuccessEmbed().createEmbed(description);
		Optional<User> user = message.getUserAuthor();
		if(message.getChannel().canYouWrite())
			message.getChannel().sendMessage(embed);
		else if(user.isPresent())
			user.get().sendMessage(embed);
	}
	
	private void sendErrorMessage(String description) {
		EmbedBuilder embed = new ErrorEmbed().createEmbed(description);
		Optional<User> user = message.getUserAuthor();
		if(message.getChannel().canYouWrite())
			message.getChannel().sendMessage(embed);
		else if(user.isPresent())
			user.get().sendMessage(embed);
	}
}
