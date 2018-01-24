package com.bot.twitch.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import me.philippheuer.twitch4j.TwitchClient;

public class OfflineTwitchNotification extends AbstractTwitchNotification {

	public OfflineTwitchNotification(Message message, TwitchClient client) {
		super(message, client);
	}

	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getTwitchPurple())
			.setAuthor("Twitch: NCLGamingSociety", getURL(), getProfileImage())
			.setDescription("We have stopped streaming " + getGame() + ". Thank you for tuning in!")
			.setFooter(getFooter());
		
		return output;
	}
}
