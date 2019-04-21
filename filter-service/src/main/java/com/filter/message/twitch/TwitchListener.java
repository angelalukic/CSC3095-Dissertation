package com.filter.message.twitch;

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
public class TwitchListener {
	
	@Id
	private long id;
	private String name;
	
	@ManyToMany
	@JoinTable(
			name = "twitch_words",
			joinColumns = @JoinColumn(name = "twitch_id"),
			inverseJoinColumns = @JoinColumn(name = "word_id"))
	@JsonIgnore
	private Set<Word> words;
	
	public TwitchListener() {
	}

	public TwitchListener(TwitchListener listener) {
		this.id = listener.getId();
		this.name = listener.getName();		
	}
}
