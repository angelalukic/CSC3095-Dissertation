package com.bot.twitch.response;

import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.message.embed.EmbedBuilder;
import me.philippheuer.twitch4j.TwitchClient;
import me.philippheuer.twitch4j.events.event.channel.HostOnEvent;

public class HostTwitchNotification extends AbstractTwitchNotification {
	
	private HostOnEvent event;

	public HostTwitchNotification(Message message, TwitchClient client, HostOnEvent event) {
		super(message, client);
		this.event = event;
	}

	public EmbedBuilder makeEmbed() {
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(getTwitchPurple())
			.setAuthor("Twitch: NCLGamingSociety", getURL(), getProfileImage())
			.setDescription("We have started hosting " + event.getTargetChannel().getDisplayName() + " playing \"" + event.getTargetChannel().getGame() 
			+ "\". Please join us here: " + getURL())
			.setFooter(getFooter());
		
		return output;
	}
}
