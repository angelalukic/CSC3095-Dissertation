package com.bot.twitch.beans;

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
	
	public TwitchStream() {
	}
	
	public TwitchStream(Stream stream) {
		this.id = stream.getId();
		this.title = stream.getTitle();
		this.thumbnailUrl = stream.getThumbnailUrl(1280, 720);
		this.viewerCount = stream.getViewerCount();
	}
}
