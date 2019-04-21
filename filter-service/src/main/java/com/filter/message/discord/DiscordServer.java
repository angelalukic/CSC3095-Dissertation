package com.filter.message.discord;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filter.words.Word;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class DiscordServer {
	
	@Id
	private long id;
	private String name;
	
	@ManyToMany
	@JoinTable(
			name = "discord_words",
			joinColumns = @JoinColumn(name = "discord_id"),
			inverseJoinColumns = @JoinColumn(name = "word_id"))
	@JsonIgnore
	private Set<Word> words;
	
	public DiscordServer() {
	}
	
	public DiscordServer(DiscordServer discordServer) {
		this.id = discordServer.getId();
		this.name = discordServer.getName();
	}
}
