package com.bot.filter.response;

import java.awt.Color;

import de.btobastian.javacord.entities.message.Message;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractFilterNotification implements FilterNotification {
	
	private Message message;
	private String flaggedWord;
	private Color red = new Color(255,0,0);
	private Color orange = new Color(255,165,0);
	private Color yellow = new Color(255,255,0);
	
	public AbstractFilterNotification(Message message, String flaggedWord) {
		this.message = message;
		this.flaggedWord = flaggedWord;		
	}
}
