package com.bot.twitch.game;

import java.util.Arrays;
import java.util.List;
import com.github.twitch4j.TwitchClient;
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
	
	public TwitchGame(String gameId, TwitchClient client) {
		Game game = retrieveGame(gameId, client);
		this.name = game.getName();
		this.id = game.getId();
		this.art = game.getBoxArtUrl().replace("{width}", "188").replace("{height}", "250");
	}
	
	private Game retrieveGame(String gameId, TwitchClient client) {
		
		List<Game> games = client.getHelix().getGames(Arrays.asList(gameId), null).execute().getGames();
		
		for(int i = 0; i < games.size(); i++) { 
			Game game = games.get(i);
			if(gameId.equalsIgnoreCase(game.getId()))
				return games.get(i);
		}
		throw new TwitchGameNotFoundException("id-" + gameId);
	}
}
