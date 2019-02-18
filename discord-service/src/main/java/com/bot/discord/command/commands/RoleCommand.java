package com.bot.discord.command.commands;

import java.util.List;
import java.util.Optional;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
import org.javacord.api.entity.user.User;

import com.bot.discord.exception.UserNotFoundException;
import com.bot.discord.exception.ServerNotFoundException;

public class RoleCommand {
	
	private Message message;
	
	public RoleCommand(Message message) {
		this.message = message;
	}
	
	public void execute() {
		String role = message.getContent().substring(8);
		Optional<Server> serverOptional = message.getServer(); 
		
		if(serverOptional.isPresent()) {
			Server server = serverOptional.get();
			testRolePresent(server, role);
		}
		else 
			throw new ServerNotFoundException("id=" + message.getId());
	}
	
	private void testRolePresent(Server server, String r) {
		List<Role> roles = server.getRolesByNameIgnoreCase(r);
		
		if(roles.isEmpty())
			message.getChannel().sendMessage("Role not found");
		else {
			Role role = roles.get(0);
			testValidRole(server, role);
		}
	}
	
	private void testValidRole(Server server, Role role) {
		if(role.getColor().isPresent() || role.getAllowedPermissions().contains(PermissionType.ADMINISTRATOR)) 
			message.getChannel().sendMessage("You do not have permission to get this role");
		else 
			testValidUser(server, role);
	}
		
	private void testValidUser(Server server, Role role) {
		Optional<User> userOptional = message.getUserAuthor();
		if(userOptional.isPresent()) {
			User user = userOptional.get();
			assignRole(server, user, role);
		}
		else 
			throw new UserNotFoundException("id=" + message.getId());
	}
	
	private void assignRole(Server server, User user, Role role) {
		if(user.getRoles(server).contains(role)) {
			user.removeRole(role);
			message.getChannel().sendMessage("Role removed");
		}
		else {
			user.addRole(role);
			message.getChannel().sendMessage("Role added");
		}
	}
}
