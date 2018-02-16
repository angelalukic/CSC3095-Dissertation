package com.bot.utils;

import java.awt.Color;
import java.util.Collection;

import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RoleAssigner {
	
	private Message message;
	private Color defaultColour = new Color(0, 0, 0);
	private boolean roleExists;
	
	public RoleAssigner(Message message) {
		this.message = message;
	}
	
	public void assignRole(String requestedRole) {
		
		User user = message.getAuthor();
		Collection<Role> roles = message.getChannelReceiver().getServer().getRoles();
		roleExists = false;
		
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase(requestedRole)) {
				
				roleExists = true;
				
				log.info(role.getColor().getRed() + " " + role.getColor().getGreen() + " " + role.getColor().getBlue());
				
				if(role.getColor().getRGB() == defaultColour.getRGB()) {
					
					if(hasRole(requestedRole)) {
						role.removeUser(user);
						message.reply("The " + requestedRole + " role has been removed.");
					} else {
						role.addUser(user);
						message.reply("The " + requestedRole + " role has been added.");
					}
				} else {
					message.reply("You cheeky thing! I'm not giving you the " + requestedRole + " role!");
				}
			}
		}
		
		if(!roleExists) {
			message.reply("I don't think that role exists, sorry :[");
		}
	}
	
	public boolean hasRole(String requestedRole) {
		
		User user = message.getAuthor();
		Server server = message.getChannelReceiver().getServer();
		
		Collection<Role> roles = user.getRoles(server);
		
		for(Role role : roles) {
			if(role.getName().equalsIgnoreCase(requestedRole)) {
				return true;
			} 			
		}
		return false;		
	}
}
