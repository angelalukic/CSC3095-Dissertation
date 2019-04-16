package com.bot.discord.command.commands.admin;

import java.awt.Color;
import java.util.List;
import java.util.Optional;

import org.beryx.awt.color.ColorFactory;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.permission.PermissionType;
import org.javacord.api.entity.permission.Role;
import org.javacord.api.entity.server.Server;
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
import com.bot.discord.server.DiscordServerRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RoleAdminCommand {
	
	@Autowired private DiscordServerRepository serverRepository;
	@Autowired private DiscordRoleRepository roleRepository;
	@Autowired private DiscordUtils utils;
	@Autowired private ErrorEmbed errorEmbed;
	@Autowired private SuccessEmbed successEmbed;
	private MessageCreateEvent event;
	private Server server;
	
	private static final String ADD_COMMAND = "\n`rf@role add <role>`";
	private static final String REMOVE_COMMAND = "\n`rf@role remove <role>`";
	private static final String COLOUR_COMMAND = "\n`rf@role colour <colour>`";
	private static final String CLEAR_COLOUR_COMMAND = "\n`rf@role clearcolour <colour>`";
	private static final String HELP_COMMAND = "\n\nFor more information use the following command:\n`rf@role help`";
	
	public void execute(MessageCreateEvent event) {
		this.event = event;
		server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		Message message = event.getMessage();
		try {
			String command = message.getContent().substring(8);																	// Can throw IndexOutOfBoundsException
			DiscordServer discordServer = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId());	// Can throw ServerNotFoundException
			if(command.startsWith("help"))
				sendHelpCommandMessage();
			else if(command.startsWith("add")) {
				String role = message.getContent().substring(12);																// Can throw IndexOutOfBoundsException
				addRoleToRepository(discordServer, role);							
			}
			else if(command.startsWith("remove")) {
				String role = message.getContent().substring(15);																// Can throw IndexOutOfBoundsException
				removeRoleFromRepository(role);
			}
			else if(command.startsWith("colour")) {
				String colour = message.getContent().substring(15);																// Can throw IndexOutOfBoundsException
				updateColourInRepository(discordServer, colour);																// Can throw IllegalArgumentException
			}
			else if(command.startsWith("clearcolour"))
				clearColour(discordServer);
			else
				sendInvalidCommandErrorMessage();
		}
		catch(IndexOutOfBoundsException e) {
			sendInvalidCommandErrorMessage();
		}
		catch(ServerNotFoundException e) {
			sendServerNotRegisteredErrorMessage();
		}
		catch(IllegalArgumentException e) {
			sendInvalidColourErrorMessage();
		}
	}
	
	private void addRoleToRepository(DiscordServer discordServer, String role) {
		List<Role> roles = server.getRolesByNameIgnoreCase(role);
		if(roles.isEmpty())
			sendInvalidRoleErrorMessage();
		else {
			DiscordRole savedRole = new DiscordRole(roles.get(0));
			if(roles.get(0).getAllowedPermissions().contains(PermissionType.ADMINISTRATOR))
				sendInvalidRoleErrorMessage();
			else {
				savedRole.setServer(discordServer);
				roleRepository.save(savedRole);
				sendRoleSuccessfullyAddedMessage(savedRole);
			}
		}
	}
	
	private void removeRoleFromRepository(String role) {
		List<Role> roles = server.getRolesByNameIgnoreCase(role);
		if(roles.isEmpty())
			sendInvalidRoleErrorMessage();
		else {
			long id = roles.get(0).getId();
			Optional<DiscordRole> optionalRole = roleRepository.findById(id);
			if(optionalRole.isPresent()) {
				DiscordRole savedRole = optionalRole.get();
				roleRepository.deleteById(id);
				sendRoleSuccessfullyDeletedMessage(savedRole);
			}
			else
				sendInvalidRoleErrorMessage();
		}
	}
	
	private void updateColourInRepository(DiscordServer discordServer, String colour) {
		String oldColour = discordServer.getRoleColour();
		discordServer.setRoleColour(colour);
		serverRepository.save(discordServer);
		sendColourSuccessfullyUpdatedMessage(oldColour, colour);
	}
	
	private void clearColour(DiscordServer discordServer) {
		String defaultColour = "#99aab5";
		String oldColour = discordServer.getRoleColour();
		discordServer.setRoleColour(defaultColour);
		serverRepository.save(discordServer);
		sendColourSuccessfullyUpdatedMessage(oldColour, defaultColour);
	}
	
	private void sendInvalidCommandErrorMessage() {
		log.error("[" + server.getName() + "] Invalid Role Admin Command");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Valid commands are as follows:"
				+ ADD_COMMAND + REMOVE_COMMAND + COLOUR_COMMAND + CLEAR_COLOUR_COMMAND + HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendServerNotRegisteredErrorMessage() {
		log.error("[" + server.getName() + "] Error on Role Admin Command: Server Not Registered In Database");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Server Error**: Server has not been registered, please use command `rf@register`"
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidColourErrorMessage() {
		log.error("[" + server.getName() + "] Error on Role Admin Command: Invalid Colour");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Command**: Please provide a valid colour, for example: blue, #aa38e0, 0x40A8CC, etc."
				+ HELP_COMMAND);
		utils.sendMessage(embed, event);
	}
	
	private void sendInvalidRoleErrorMessage() {
		log.error("[" + server.getName() + "] Error on Role Admin Command: Invalid Role Provided");
		EmbedBuilder embed = errorEmbed.createEmbed(
				"**Invalid Role**: You have provided a role which either doesn't exist in your server, has administrative permissions "
				+ "or doesn't exist in our database (if you are using a deleate command).");
		utils.sendMessage(embed, event);
	}
	
	private void sendHelpCommandMessage() {
		log.info("[" + server.getName() + "] Sending Role Admin Command Help");
		EmbedBuilder embed = successEmbed.createEmbed(
			"These commands allow you to change roles which I can assign to users when they use the rf!role command."
			+ " I will never assign roles which have administrative permissions."
			+ " Only users with the 'Administrator' permission can use these commands."
			+ " Commands are as follows:\n" 
			+ ADD_COMMAND + "\n Add a role which users can assign themselves.\n"
			+ REMOVE_COMMAND + "\n Remove a role which users can assign themselves.\n" 
			+ COLOUR_COMMAND + "\n Makes it so all roles with this colour can be assigned by users to themselves. By default roles without a colour are assignable."
					+ " Valid colour formats include: blue, #aa38e0, 0x40A8CC, etc.\n" 
			+ CLEAR_COLOUR_COMMAND + "\n Makes it so roles without a colour can be assigned by users to themselves. The default option."
			+ "\n\nYour server will need to be registered with our database before you can use these commands."
			+ "To manually register use the following command:\n`rf@register`");
		utils.sendMessage(embed, event);
	}
	
	private void sendRoleSuccessfullyAddedMessage(DiscordRole role) {
		log.info("[" + server.getName() + "] Successfully added role: " + role.getName());
		EmbedBuilder embed = successEmbed.createEmbed("Role **" + role.getName() + "** can now be assigned through the `rf!role` command.");
		utils.sendMessage(embed, event);
	}
	
	private void sendRoleSuccessfullyDeletedMessage(DiscordRole role) {
		log.info("[" + server.getName() + "] Successfully deleted role: " + role.getName());
		EmbedBuilder embed = successEmbed.createEmbed("Role **" + role.getName() + "** can no longer be assigned through the `rf!role` command.");
		utils.sendMessage(embed, event);
	}
	
	private void sendColourSuccessfullyUpdatedMessage(String oldColour, String newColour) {
		log.info("[" + server.getName() + "] Successfully updated role colour: " + newColour);
		Color color = ColorFactory.valueOf(newColour);
		EmbedBuilder embed = successEmbed.createEmbed(
				"Role assignment colour has successfully been updated from " + oldColour + " to " + newColour + ".")
				.setColor(color);
		utils.sendMessage(embed, event);
	}
}
