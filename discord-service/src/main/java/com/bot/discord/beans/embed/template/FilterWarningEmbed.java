package com.bot.discord.beans.embed.template;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.filter.beans.MessageJudgement;

@Component
public class FilterWarningEmbed {
	
	@Autowired
	private DiscordUtils utils;
	
	public EmbedBuilder createEmbed(MessageCreateEvent event, MessageJudgement judgement) {
		long id = event.getMessageId();
		User user = utils.getUserFromUserOptional(event.getMessageAuthor().asUser(), id);
		List<String> wordViolations = judgement.getBlacklist();
		List<String> wordWarnings = judgement.getGreylist();
		List<String> wordWhitelist = judgement.getWhitelist();
		TextChannel channel = event.getChannel();
		
		EmbedBuilder embed = new EmbedBuilder().setTitle("Potential Word Filter Violation")
				.setDescription("Potential Word Filter Violation detected. Please react with :put_litter_in_its_place: to delete the original message and notify the user.")
				.addInlineField("Username", "<@" + user.getId() + ">")
				.addInlineField("Channel", "<#" + channel.getId() + ">")
				.addInlineField("Message ID", "" + id)
				.setColor(new Color(255, 255, 0))
				.setFooter(new Date().toString());
		if(!wordViolations.isEmpty())
			embed.addField("Blacklisted Words Detected", wordViolations.toString());
		if(!wordWarnings.isEmpty())
			embed.addField("Greylisted Words Detected", wordWarnings.toString());
		if(!wordWhitelist.isEmpty())
			embed.addField("Whitelisted Words Detected", wordWhitelist.toString());
		embed.addField("Message", event.getMessageContent());
		return embed;
	}
}
