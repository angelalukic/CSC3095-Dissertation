package com.bot.discord.beans.embed.template;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordUtils;
import com.bot.filter.beans.MessageJudgement;

@Component
public class FilterViolationEmbed {
	
	@Autowired
	private DiscordUtils utils;
	
	public EmbedBuilder createEmbedWithoutDelete(MessageCreateEvent event, MessageJudgement judgement) {
		long id = event.getMessageId();
		User user = utils.getUserFromUserOptional(event.getMessageAuthor().asUser(), id);
		List<String> wordViolations = judgement.getBlacklist();
		TextChannel channel = event.getChannel();
		return new EmbedBuilder().setTitle("Word Filter Violation")
				.setDescription("Word Filter Violation detected. Original message was automatically deleted and user has been notified.")
				.addInlineField("Username", "<@" + user.getId() + ">")
				.addInlineField("Channel", "<#" + channel.getId() + ">")
				.addInlineField("Message ID", "" + id)
				.addInlineField("Violations Detected", wordViolations.toString())
				.addField("Message", event.getMessageContent())
				.setColor(new Color(255, 0, 0))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createEmbedWithDelete(MessageCreateEvent event, MessageJudgement judgement) {
		long id = event.getMessageId();
		User user = utils.getUserFromUserOptional(event.getMessageAuthor().asUser(), id);
		List<String> wordViolations = judgement.getBlacklist();
		TextChannel channel = event.getChannel();
		return new EmbedBuilder().setTitle("Word Filter Violation")
				.setDescription("Word Filter Violation detected. **Original message could not be deleted due to insignificant permissions**.")
				.addInlineField("Username", "<@" + user.getId() + ">")
				.addInlineField("Channel", "<#" + channel.getId() + ">")
				.addInlineField("Message ID", "" + id)
				.addInlineField("Violations Detected", wordViolations.toString())
				.addField("Message", event.getMessageContent())
				.setColor(new Color(255, 0, 0))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createUserEmbed(MessageCreateEvent event) {
		long id = event.getMessageId();
		User user = utils.getUserFromUserOptional(event.getMessageAuthor().asUser(), id);
		TextChannel channel = event.getChannel();
		return new EmbedBuilder().setTitle("Word Filter Violation")
				.setDescription("Word Filter Violation detected. Your message has been automatically deleted & administrators have been notified.")
				.addInlineField("Username", "<@" + user.getId() + ">")
				.addInlineField("Channel", "<#" + channel.getId() + ">")
				.addInlineField("Message ID", "" + id)
				.addField("Message", event.getMessageContent())
				.setColor(new Color(255, 0, 0))
				.setFooter(new Date().toString());
	}
	
	public EmbedBuilder createUserEmbed(Message message) {
		String userId = message.getAuthor().getIdAsString();
		String channelId = message.getChannel().getIdAsString();
		String messageId = message.getIdAsString();
		return new EmbedBuilder().setTitle("Word Filter Violation")
				.setDescription("Word Filter Violation detected. Your message has been automatically deleted & administrators have been notified.")
				.addInlineField("Username", "<@" + userId + ">")
				.addInlineField("Channel", "<#" + channelId + ">")
				.addInlineField("Message ID", messageId)
				.addField("Message", message.getContent())
				.setColor(new Color(255, 0, 0))
				.setFooter(new Date().toString());		
	}
}
