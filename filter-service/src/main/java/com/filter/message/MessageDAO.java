package com.filter.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.filter.message.discord.DiscordMessage;
import com.filter.message.discord.DiscordServer;
import com.filter.message.discord.DiscordServerRepository;
import com.filter.words.Word;
import com.filter.words.WordsNotFoundException;

@Component
public class MessageDAO {

	@Autowired private DiscordServerRepository discordRepository;
	@Autowired private MessageUtils utils;

	public MessageJudgement assessMessage(DiscordMessage message, long discordChannel) {
		Optional<DiscordServer> optionalServer = discordRepository.findById(discordChannel);
		if(!optionalServer.isPresent())
			return new MessageJudgement(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), JudgementLevel.GREEN);
		List<Word> words = new ArrayList<>(optionalServer.get().getWords());
		if(words.isEmpty())
			throw new WordsNotFoundException("id=" + discordChannel);
		return utils.getAssessment(message.getMessage(), words);
	}
}
