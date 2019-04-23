package com.filter.words;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filter.message.JudgementLevel;
import com.filter.message.discord.DiscordServer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Word {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	private String word;
	
	@ManyToMany(mappedBy = "words")
	@JsonIgnore
	private Set<DiscordServer> servers;
	
	@Enumerated(EnumType.STRING)
	private JudgementLevel level;
	
	public Word() {
	}
	
	public Word(String word, JudgementLevel level) {
		this.word = word;
		this.level = level;
	}
}
