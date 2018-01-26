package com.bot.twitch.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.channel.HostOffEvent;

public class UnhostTwitchNotification extends AbstractTwitchNotification {
	
	private HostOffEvent event;

	public UnhostTwitchNotification(Message message, TwitchClient client, HostOffEvent event) {
		super(message, client);
		this.event = event;
	}

	public EmbedBuilder makeEmbed() {
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getTwitchPurple())
			.setAuthor("Twitch: NCLGamingSociety", getURL(), getProfileImage())
			.setDescription("We have stopped hosting. Thank you for tuning in!")
			.setFooter(getFooter());
		
		return output;
	}
}
