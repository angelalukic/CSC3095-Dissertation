package com.bot.discord.beans.role;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.javacord.api.entity.permission.Role;

import com.bot.discord.beans.server.DiscordServer;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DiscordRole {
	
	@Id	private long id;
	private String name;
	
	@ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    @JsonIgnore
	private DiscordServer server;
	
	public DiscordRole() {
	}
	
	public DiscordRole(Role role) {
		this.id = role.getId();
		this.name = role.getName();
	}
}
