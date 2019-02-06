package com.bot.listener;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DiscordServer {
	
	@Id 
	private long id;
	
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JsonIgnore
	private TwitterListener listener;
	
	public DiscordServer() {
	}

	public DiscordServer(long id, String name, TwitterListener listener) {
		this.id = id;
		this.name = name;
		this.listener = listener;
	}	
}
