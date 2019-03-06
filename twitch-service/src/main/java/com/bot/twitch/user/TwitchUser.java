package com.bot.twitch.user;

import java.util.Arrays;
import java.util.List;

import com.github.twitch4j.helix.domain.User;
import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.common.events.domain.EventChannel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TwitchUser {
	
	private String displayName;
	private long id;
	private String description;
	private String image;
	private int viewCount;
	
	public TwitchUser() {
	}
	
	public TwitchUser(EventChannel channel, TwitchClient client) {
		User user = retrieveUser(channel.getName(), client);
		this.displayName = user.getDisplayName();
		this.id = user.getId();
		this.description = user.getDescription();
		this.image = user.getProfileImageUrl();
		this.viewCount = user.getViewCount();
	}
	
	private User retrieveUser(String username, TwitchClient client) {
		
		List<User> users = client.getHelix().getUsers(null, null, Arrays.asList(username)).execute().getUsers();
		
		for(int i = 0; i < users.size(); i++) { 
			User user = users.get(i);
			if(user.getDisplayName().equalsIgnoreCase(username))
				return users.get(i);
		}
		throw new TwitchUserNotFoundException("name-" + username);
	}
}
