package com.bot.ai;

import org.javacord.api.entity.channel.TextChannel;
import org.javacord.api.event.message.MessageCreateEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bot.ai.beans.AiMessage;
import com.bot.discord.DiscordChannelConnection;
import com.bot.discord.DiscordUtils;
import com.bot.discord.beans.server.DiscordServer;
import com.bot.discord.beans.server.DiscordServerRepository;
import com.bot.discord.exception.ServerNotFoundException;

import feign.FeignException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AiDAO {
	
	@Autowired DiscordServerRepository repository;
	@Autowired AiServiceProxy proxy;	
	@Autowired private DiscordChannelConnection discordChannel;
	@Autowired private DiscordUtils utils;
	private static final String AI = "ai";
	private MessageCreateEvent event;
	
	public void sendMessageToAi(MessageCreateEvent event) {
		this.event = event;
		try {
			DiscordServer server = utils.getDiscordServerFromServerOptional(event.getServer(), event.getMessageId());
			TextChannel channel = discordChannel.connect(server.getId(), AI);
			if(server.isAiEnabled() && channel.getId() == event.getChannel().getId()) {
				AiMessage message = new AiMessage(event.getMessage());
				String reply = proxy.getReplyFromAi(message);
				sendReply(channel, reply);				
			}
		}
		catch(ServerNotFoundException e) {
			log.debug("Server is not registered.");
		}
		catch(FeignException e) {
			log.error("Connection to ai-service refused.");
		}
	}
	
	private void sendReply(TextChannel channel, String reply) {
		if(channel.getId() != 0L && channel.canYouWrite())
			channel.sendMessage(reply);
		else
			utils.sendMessageToUser(reply, event);
	}
}
