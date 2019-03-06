package com.bot.twitch.stream;

import java.util.Arrays;
import java.util.List;

import com.bot.twitch.game.TwitchGame;
import com.bot.twitch.user.TwitchUser;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Stream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchStream {
	
	private long id;
	private String title;
	private String thumbnailUrl;
	private int viewerCount;
	private TwitchUser user;
	private TwitchGame game;
	
	public TwitchStream() {
	}
	
	public TwitchStream(TwitchUser user, TwitchClient client) {
		Stream stream = retrieveStream(user.getId(), client);
		this.id = stream.getId();
		this.title = stream.getTitle();
		this.thumbnailUrl = stream.getThumbnailUrl(1280, 720);
		this.viewerCount = stream.getViewerCount();
		this.user = user;
		this.game = new TwitchGame(Long.toString(stream.getGameId()), client);
	}
	
	private Stream retrieveStream(long userId, TwitchClient client) {
		
		List<Stream> streams = client.getHelix().getStreams("", "", null, null, null, null, Arrays.asList(userId), null).execute().getStreams();
		
		for(int i = 0; i < streams.size(); i++) { 
			Stream stream = streams.get(i);
			if(userId == stream.getUserId())
				return streams.get(i);
		}
		throw new TwitchStreamNotFoundException("id-" + userId);
	}
}
