package com.bot.twitch.game;

import com.github.twitch4j.helix.domain.Game;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchGame {
	
	private String name;
	private String id;
	private String art;
	
	public TwitchGame() {
	}
	
	public TwitchGame(Game game) {
		this.name = game.getName();
		this.id = game.getId();
		this.art = game.getBoxArtUrl().replace("{width}", "188").replace("{height}", "250");
	}
}
