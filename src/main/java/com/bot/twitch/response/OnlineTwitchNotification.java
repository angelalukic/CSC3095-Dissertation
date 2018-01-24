package com.bot.twitch.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import me.philippheuer.twitch4j.TwitchClient;

public class OnlineTwitchNotification extends AbstractTwitchNotification {

	public OnlineTwitchNotification(Message message, TwitchClient client) {
		super(message, client);
	}

	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getTwitchPurple())
			.setAuthor("Twitch: NCLGamingSociety", getURL(), getProfileImage())
			.setDescription("We have started streaming " + getGame() + " - \"" 
				+ getStreamName() + "\". Please join us here: " + getURL())
			.setFooter(getFooter());
		
		return output;
	}
}
