package com.bot.discord.command.commands;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import org.beryx.awt.color.ColorFactory;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.discord.embed.template.ErrorEmbed;
import com.bot.discord.embed.template.SuccessEmbed;
import com.bot.discord.exception.ServerNotFoundException;
import com.bot.discord.role.DiscordRole;
import com.bot.discord.role.DiscordRoleRepository;
import com.bot.discord.server.DiscordServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoleCommand {
	
	@Autowired DiscordRoleRepository roleRepository;
	@Autowired DiscordUtils utils;
	@Autowired ErrorEmbed errorEmbed;
	@Autowired SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private User user;
	private Role role;
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId());	// Can throw ServerNotFoundException
			role = getRole(discordServer, server, message.getContent().substring(8));
			
			if(role != null) {
				user = utils.getUserFromUserOptional(event.getMessage().getUserAuthor(), event.getMessageId());
				assignRole(server, user, role);
			}
			else 
				sendInvalidRoleErrorMessage();
		}
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();
		}
	}
	
	private Role getRole(DiscordServer discordServer, Server server, String r) {
		List<Role> roles = server.getRolesByNameIgnoreCase(r);
		if(roles.isEmpty()) {
			return null;
		}
		else {
			Role temp = roles.get(0);
			if(!isValidRole(discordServer, temp))
				return null;
			return temp;
		}
	}
	
	private boolean isValidRole(DiscordServer server, Role role) {
		Color validColor = ColorFactory.valueOf(server.getRoleColour());
		Optional<DiscordRole> discordRole = roleRepository.findById(role.getId());
		if(role.getAllowedPermissions().contains(PermissionType.ADMINISTRATOR))
			return false;
		if(discordRole.isPresent())
			return true;
		if(role.getColor().isPresent()) {
			Color color = role.getColor().get();
			if(color.equals(validColor))
					return true;
		}
		else if(validColor.equals(new Color(153,170,181)))
			return true;
		return false;		
	}

	private void assignRole(Server server, User user, Role role) {
		if(user.getRoles(server).contains(role)) {
			user.removeRole(role);
			sendRoleSuccessfullyRemovedMessage();
		}
		else {
			user.addRole(role);
			sendRoleSuccessfullyAddedMessage();
		}
	}
	
	private void sendRoleSuccessfullyRemovedMessage() {
		log.info("[" + server.getName() + "] " + role.getName() + " removed from user " + user.getName());
		EmbedBuilder embed = successEmbed.createEmbed("\"" + role.getName() + "\" has been successfully removed from " + user.getName() + ".");
		utils.sendMessage(embed, event);
	}
	
	private void sendRoleSuccessfullyAddedMessage() {
		log.info("[" + server.getName() + "] " + role.getName() + " assigned to user " + user.getName());
		EmbedBuilder embed = successEmbed.createEmbed("\"" + role.getName() + "\" has been successfully assigned from " + user.getName() + ".");
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidRoleErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Role Command");
		EmbedBuilder embed = errorEmbed.createEmbed("**Invalid Command**: Invalid role provided, this role may not exist or the role is not assignable.");
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Role Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please contact the administrator of the server and request they use command `rf@register`");
		utils.sendMessage(embed, event);
	}
}
