package com.bot.twitter.status;

import java.util.Date;

import com.bot.twitter.TwitterUser;

import lombok.Getter;
import lombok.Setter;
import twitter4j.MediaEntity;
import twitter4j.MediaEntity.Variant;
import twitter4j.Status;

/*
 * @Getter and @Setter automatically insert Getter and Setter Methods
 * See: https://projectlombok.org/features/GetterSetter
 */

@Getter
@Setter
public class TwitterStatus {
	
	private TwitterUser user;
	private long id;
	private String body;
	private Date date;
	private String image;
	private String video;
	private boolean isRetweet;
	
	public TwitterStatus() {
	}
	
	public TwitterStatus(Status status) {
		this.user = new TwitterUser(status.getUser());
		this.id = status.getId();
		this.body = status.getText();
		this.date = status.getCreatedAt();
		this.image = retrieveImageURL(status);
		this.video = retrieveVideoURL(status);
		this.isRetweet = status.isRetweet();		
	}
	
	public TwitterStatus(TwitterStatus status) {
		this.user = status.getUser();
		this.id = status.getId();
		this.body = status.getBody();
		this.date = status.getDate();
		this.image = status.getImage();
		this.video = status.getVideo();
		this.isRetweet = status.isRetweet();
	}
	
	private String retrieveImageURL(Status status) {
		
		MediaEntity[] entities = status.getMediaEntities();
		
		if(entities.length > 0 && entities[0] != null)
			return entities[0].getMediaURL();
		
		return null;
	}
	
	private String retrieveVideoURL(Status status) {
		
		MediaEntity[] entities = status.getMediaEntities();
		
		if(entities.length > 0 && entities[0] != null) {
			
			MediaEntity entity = entities[0];
			Variant[] variants = entity.getVideoVariants();
				
			if(variants.length > 0 && variants[0] != null )
				return entity.getVideoVariants()[0].getUrl();
		}
		return null;
	}
}
