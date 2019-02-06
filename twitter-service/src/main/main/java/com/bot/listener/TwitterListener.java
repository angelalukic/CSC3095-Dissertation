package com.bot.listener;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class TwitterListener {
	
	@Id
	private long id;
	
	private String name;	
	
	@OneToMany(mappedBy="listener")
	private List<DiscordServer> servers;
	
	public TwitterListener() {
	}

	public TwitterListener(long id, String name, List<DiscordServer> servers) {
		this.id = id;
		this.name = name;
		this.servers = servers;
	}
}
