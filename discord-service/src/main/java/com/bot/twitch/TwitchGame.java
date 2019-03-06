package com.bot.twitch;

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
	
	public TwitchGame(TwitchGame game) {
		this.name = game.getName();
		this.id = game.getId();
		this.art = game.getArt();
	}
}
