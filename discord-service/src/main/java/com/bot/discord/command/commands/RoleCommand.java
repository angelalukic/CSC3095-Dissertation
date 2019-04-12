package com.bot.discord.command.commands;

import java.util.List;

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

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoleCommand {
	
	@Autowired DiscordUtils utils;
	@Autowired ErrorEmbed errorEmbed;
	@Autowired SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	private User user;
	private Role role;
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		Message message = event.getMessage();
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		role = getRole(server, message.getContent().substring(8));
		
		if(role != null) {
			user = utils.getUserFromUserOptional(event.getMessage().getUserAuthor(), event.getMessageId());
			assignRole(server, user, role);
		}
		else 
			sendInvalidRoleErrorMessage();
	}
	
	private Role getRole(Server server, String r) {
		List<Role> roles = server.getRolesByNameIgnoreCase(r);
		if(roles.isEmpty()) {
			return null;
		}
		else {
			Role temp = roles.get(0);
			if(temp.getColor().isPresent() || temp.getAllowedPermissions().contains(PermissionType.ADMINISTRATOR))
				return null;
			return temp;
		}
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
}
