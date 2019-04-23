package com.filter.subscription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.filter.message.JudgementLevel;
import com.filter.message.discord.DiscordServer;
import com.filter.message.discord.DiscordServerRepository;
import com.filter.message.twitch.TwitchListener;
import com.filter.message.twitch.TwitchListenerRepository;
import com.filter.words.Word;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional
public class SubscriptionDAO {
	
	@Autowired private DiscordServerRepository discordRepository;
	@Autowired private TwitchListenerRepository twitchRepository;

	public DiscordServer addToBlacklist(DiscordServer server, String input) {
		Word word = new Word(input, JudgementLevel.RED);
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return addWordToServer(server, word);
	}

	public ResponseEntity<Object> removeFromBlacklist(DiscordServer discordServer, String input) {
		DiscordServer server = getServerInRepository(discordServer.getId());
		Word word = new Word(input, JudgementLevel.RED);
		if(server == null)
			return ResponseEntity.notFound().build();
		boolean wordDeleted = deleteWordFromDiscordServer(server, word);
		if(!wordDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}
	
	public DiscordServer addToGreylist(DiscordServer server, String input) {
		Word word = new Word(input, JudgementLevel.YELLOW);
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return addWordToServer(server, word);
	}

	public ResponseEntity<Object> removeFromGreylist(DiscordServer discordServer, String input) {
		DiscordServer server = getServerInRepository(discordServer.getId());
		Word word = new Word(input, JudgementLevel.YELLOW);
		if(server == null)
			return ResponseEntity.notFound().build();
		boolean wordDeleted = deleteWordFromDiscordServer(server, word);
		if(!wordDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}

	public DiscordServer addToWhitelist(DiscordServer server, String input) {
		Word word = new Word(input, JudgementLevel.GREEN);
		Optional<DiscordServer> optionalServer = discordRepository.findById(server.getId());
		if(!optionalServer.isPresent()) {
			discordRepository.save(server);
			log.info("Server " + server.getName() + " did not exist in repository. New Entry Created.");
		}
		return addWordToServer(server, word);
	}

	public ResponseEntity<Object> removeFromWhitelist(DiscordServer discordServer, String input) {
		DiscordServer server = getServerInRepository(discordServer.getId());
		Word word = new Word(input, JudgementLevel.GREEN);
		if(server == null)
			return ResponseEntity.notFound().build();
		boolean wordDeleted = deleteWordFromDiscordServer(server, word);
		if(!wordDeleted)
			return ResponseEntity.notFound().build();
		return ResponseEntity.ok().build();
	}

	public ResponseEntity<Object> syncToTwitchChannel(DiscordServer discordServer, long channel) {
		DiscordServer server = getServerInRepository(discordServer.getId());
		if(server == null)
			return ResponseEntity.notFound().build();
		List<Word> words = new ArrayList<>(server.getWords());
		if(words.isEmpty())
			return ResponseEntity.notFound().build();
		TwitchListener listener = getListenerInRepository(channel);
		if(listener == null)
			listener = twitchRepository.save(new TwitchListener(channel));
		syncWords(listener, words);
		return ResponseEntity.ok().build();
	}
	
	private DiscordServer getServerInRepository(long id) {
		Optional<DiscordServer> optionalServer = discordRepository.findById(id);
		if(optionalServer.isPresent())
			return optionalServer.get();
		log.error("Server with ID " + id + " doesn't exist in repository.");
		return null;
	}
	
	private TwitchListener getListenerInRepository(long id) {
		Optional<TwitchListener> optionalListener = twitchRepository.findById(id);
		if(optionalListener.isPresent())
			return optionalListener.get();
		log.error("Listener with ID " + id + " doesn't exist in repository.");
		return null;
	}
	
	private boolean deleteWordFromDiscordServer(DiscordServer server, Word input) {
		List<Word> words = new ArrayList<>(server.getWords());
		for(int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			if(word.getWord().equalsIgnoreCase(input.getWord()) && word.getLevel() == input.getLevel()) {
				words.remove(i);
				Set<Word> updatedWords = new HashSet<>(words);
				server.setWords(updatedWords);
				discordRepository.save(server);
				log.info("Word " + input.getWord() + " will no longer be flagged in Discord Server " + server.getName());
				return true;
			}
		}
		log.error("Word " + input.getWord() + " is not registered for Discord Server " + server.getName());
		return false;
	}
	
	private DiscordServer addWordToServer(DiscordServer discordServer, Word word) {
		DiscordServer repositoryServer = discordRepository.getOne(discordServer.getId());
		Set<Word> words = repositoryServer.getWords();
		if(words == null)
			words = new HashSet<>();
		words.add(word);
		repositoryServer.setWords(words);	
		DiscordServer savedServer = discordRepository.save(repositoryServer);
		log.info("Discord Server database entry updated for: " + word.getWord());
		return savedServer;
	}
	
	private void syncWords(TwitchListener listener, List<Word> words) {
		Set<Word> twitchWords = new HashSet<>();
		for(int i = 0; i < words.size(); i++)
			twitchWords.add(words.get(i));
		listener.setWords(twitchWords);
		twitchRepository.save(listener);
		log.info(words.size() + " words synced with Twitch Listener with ID: " + listener.getId());
	}
}