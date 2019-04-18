package com.bot.twitch.beans;

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
	
	public TwitchStream(TwitchStream stream) {
		this.id = stream.getId();
		this.title = stream.getTitle();
		this.thumbnailUrl = stream.getThumbnailUrl();
		this.viewerCount = stream.getViewerCount();	
		this.user = stream.getUser();
		this.game = stream.getGame();
	}
}
