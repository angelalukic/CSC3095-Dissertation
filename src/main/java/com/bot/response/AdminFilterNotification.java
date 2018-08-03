package com.bot.response;

import java.awt.Color;
import java.util.List;

import org.javacord.api.entity.message.Message;

public class AdminFilterNotification extends AbstractNotification {

	public AdminFilterNotification(Message message, List<String> flaggedWords) {
		super(message, flaggedWords);
	}

	public void send(Color color) {
	}
}
