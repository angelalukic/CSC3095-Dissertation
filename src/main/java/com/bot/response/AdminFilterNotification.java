package com.bot.response;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class AdminFilterNotification extends AbstractNotification {

	public AdminFilterNotification(Message message, Map<String, Object> config, List<String> flaggedWords) {
		super(message, config, flaggedWords);
	}

	public void send(Color color) {
		
		String flaggedWords = getFlaggedWords().toString();
		String userId = getMessage().getAuthor().getIdAsString();
		String channelId = getMessage().getChannel().getIdAsString();
		String messageContent = getMessage().getContent();
		
		EmbedBuilder output = new EmbedBuilder();
		
		output.setColor(color);
		output.setDescription("<@!" + userId + "> has been caught saying " + flaggedWords
				+ " in channel <#" + channelId + ">.");
		output.addField("Original Message", messageContent);
		output.setFooter("To change the channel these reports get send to use: command rf@reportschannel <channel>");
		
		String reportsChannel = retrieveReportsChannel().getIdAsString();
		
		getMessage().getServer().get().getTextChannelById(reportsChannel).get().sendMessage(output);
	}
}
