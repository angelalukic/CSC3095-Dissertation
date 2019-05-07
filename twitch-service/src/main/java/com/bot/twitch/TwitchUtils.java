package com.bot.twitch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bot.discord.beans.server.DiscordServer;
import com.bot.twitch.beans.listener.TwitchListener;
import com.bot.twitch.beans.listener.TwitchListenerRepository;
import com.bot.twitch.exception.TwitchGameNotFoundException;
import com.bot.twitch.exception.TwitchStreamNotFoundException;
import com.bot.twitch.exception.TwitchUserNotFoundException;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Game;
import com.github.twitch4j.helix.domain.Stream;
import com.github.twitch4j.helix.domain.User;

@Component
@Transactional
public class TwitchUtils {
	
	@Autowired private TwitchListenerRepository twitchRepository;
	
	public List<DiscordServer> retrieveDiscordServersForTwitchListener(long id) {
		List<TwitchListener> listeners = twitchRepository.findAll();
		List<DiscordServer> servers = new ArrayList<>();
		for(int i = 0; i < listeners.size(); i++) {
			TwitchListener listener = listeners.get(i);
			if(listener.getId() == id)
				servers.addAll(listener.getServers());
		}
		return servers;
	}
	
	public Game getGameFromHelix(String gameId, TwitchClient client, String authToken) {
		List<Game> games = client.getHelix().getGames(authToken, Arrays.asList(gameId), null).execute().getGames();
		for(int i = 0; i < games.size(); i++) { 
			Game game = games.get(i);
			if(gameId.equalsIgnoreCase(game.getId()))
				return games.get(i);
		}
		throw new TwitchGameNotFoundException("id-" + gameId);
	}
	
	public User getUserFromHelix(String username, TwitchClient client) {
		List<User> users = client.getHelix().getUsers(null, null, Arrays.asList(username)).execute().getUsers();
		for(int i = 0; i < users.size(); i++) { 
			User user = users.get(i);
			if(user.getDisplayName().equalsIgnoreCase(username))
				return users.get(i);
		}
		throw new TwitchUserNotFoundException("name-" + username);
	}
	
	public Stream getStreamFromHelix(long userId, TwitchClient client, String authToken) {
		List<Stream> streams = client.getHelix().getStreams(authToken, null, null, null, null, null, null, Arrays.asList(userId), null).execute().getStreams();
		for(int i = 0; i < streams.size(); i++) { 
			Stream stream = streams.get(i);
			if(userId == stream.getUserId())
				return streams.get(i);
		}
		throw new TwitchStreamNotFoundException("id-" + userId);
	}

}
