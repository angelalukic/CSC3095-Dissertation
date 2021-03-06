package com.bot.filter;

import java.util.concurrent.ExecutionException;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.entity.server.Server;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.DiscordMessage;
import com.bot.discord.beans.embed.template.FilterViolationEmbed;
import com.bot.discord.beans.embed.template.FilterWarningEmbed;
import com.bot.filter.beans.JudgementLevel;
import com.bot.filter.beans.MessageJudgement;
import com.bot.twitch.beans.events.TwitchChatMessage;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class FilterDAO {
	
	@Autowired FilterViolationEmbed violationEmbed;
	@Autowired FilterWarningEmbed warningEmbed;
	@Autowired FilterServiceProxy proxy;
	@Autowired DiscordChannelConnection connection;
	@Autowired DiscordUtils utils;
	
	public void assessMessage(MessageCreateEvent event) {
		DiscordMessage message = new DiscordMessage(event.getMessageId(), event.getMessage().toString());
		Server server = utils.getServerFromServerOptional(event.getServer(), event.getMessageId());
		try {
			MessageJudgement judgement = proxy.checkMessage(message, server.getId());
			if(judgement.getJudgement() == JudgementLevel.RED)
				sendViolationMessages(server.getId(), event, judgement);
			else if(judgement.getJudgement() == JudgementLevel.YELLOW)
				sendWarningMessages(server.getId(), event, judgement);
		}
		catch (InterruptedException | ExecutionException e) {
			log.error("Exception thrown when trying to send Violation Messages");
			 Thread.currentThread().interrupt();
		}
		catch(FeignException e) {
			log.error("Connection to filter-service refused.");
		}
	}
	
	public void assessMessage(TwitchChatMessage event, long server) {
		DiscordMessage message = new DiscordMessage(event.getChannel().getId(), event.getMessage());
		MessageJudgement judgement = proxy.checkMessage(message, server);
		try {
			if(judgement.getJudgement() == JudgementLevel.RED)
				sendViolationMessages(server, event, judgement);
			else if(judgement.getJudgement() == JudgementLevel.YELLOW)
				sendWarningMessages(server, event, judgement);
		}
		catch (InterruptedException | ExecutionException e) {
			log.error("Exception thrown when trying to send Violation Messages");
			 Thread.currentThread().interrupt();
		}
	}

	private void sendViolationMessages(long server, MessageCreateEvent event, MessageJudgement judgement) throws InterruptedException, ExecutionException {
		EmbedBuilder embed;
		if(event.getMessage().canYouDelete()) {
			event.getMessage().delete("Automatic Word Filter");
			embed = violationEmbed.createEmbedWithoutDelete(event, judgement);
		}
		else 
			embed = violationEmbed.createEmbedWithDelete(event, judgement);
		EmbedBuilder userEmbed = violationEmbed.createUserEmbed(event);
		notifyWordFilterChannel(server, embed);
		utils.sendMessageToUser(userEmbed, event);
	}
	
	private void sendViolationMessages(long server, TwitchChatMessage event, MessageJudgement judgement) throws InterruptedException, ExecutionException {
		EmbedBuilder embed = violationEmbed.createEmbed(event, judgement);
		notifyWordFilterChannel(server, embed);
	}
	
	private void sendWarningMessages(long server, MessageCreateEvent event, MessageJudgement judgement) throws InterruptedException, ExecutionException {
		EmbedBuilder embed = warningEmbed.createEmbed(event, judgement);
		notifyWordFilterChannel(server, embed);
	}
	
	private void sendWarningMessages(long server, TwitchChatMessage event, MessageJudgement judgement) throws InterruptedException, ExecutionException {
		EmbedBuilder embed = warningEmbed.createEmbed(event, judgement);
		notifyWordFilterChannel(server, embed);
	}
	
	private void notifyWordFilterChannel(long server, EmbedBuilder embed)	throws InterruptedException, ExecutionException {
		TextChannel channel = connection.connect(server, "wordfilter");
		if(channel.getId() != 0L && channel.canYouWrite())
			channel.sendMessage(embed);
		else
			utils.sendMessageToServerOwner(channel, embed);
	}
}
