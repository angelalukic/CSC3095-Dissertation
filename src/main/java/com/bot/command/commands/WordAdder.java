package com.bot.command.commands;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.javacord.api.entity.message.Message;

import com.bot.yaml.YamlWriter;

public class WordAdder {
	
	private Message message;
	private Map<String, List<String>> wordData;
	private Map<String, Object> serverData;
	
	public WordAdder(Message message, Map<String, List<String>> wordData, Map<String, Object> serverData) {
		this.message = message;
		this.wordData = wordData;
		this.serverData = serverData;
	}
	
	public void addHighUrgencyWords(String words) throws IOException {
		
		String serverId = message.getServer().get().getIdAsString();
		List<String> wordsList = convertToList(words);
		
		wordData.put("high urgency", wordsList);
		serverData.put("word filter", wordData);
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.add(serverData);
		
		message.getChannel().sendMessage(wordsList.toString() + " has been added as high urgency (red) words.");
	}
	
	public void addMediumUrgencyWords(String words) throws IOException {
		
		String serverId = message.getServer().get().getIdAsString();
		List<String> wordsList = convertToList(words);
		
		wordData.put("medium urgency", wordsList);
		serverData.put("word filter", wordData);
		
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.add(serverData);
		
		message.getChannel().sendMessage(wordsList.toString() + " has been added as medium urgency (orange) words.");
	}
	
	public void addLowUrgencyWords(String words) throws IOException {
		
		String serverId = message.getServer().get().getIdAsString();
		List<String> wordsList = convertToList(words);
		
		wordData.put("low urgency", wordsList);
		serverData.put("word filter", wordData);
		
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.add(serverData);
		
		message.getChannel().sendMessage(wordsList.toString() + " has been added as low urgency (yellow) words.");
	}
	
	public void addWhitelistWords(String words) throws IOException {
		
		String serverId = message.getServer().get().getIdAsString();
		List<String> wordsList = convertToList(words);
		
		wordData.put("whitelist", wordsList);
		serverData.put("word filter", wordData);
		
		YamlWriter yamlWriter = new YamlWriter("Servers\\" + serverId + ".yml");
		yamlWriter.add(serverData);
		
		message.getChannel().sendMessage(wordsList.toString() + " has been added as whitelisted words.");
	}
	
	private List<String> convertToList(String words) {
		String noWhitespaceWords = removeWhiteSpace(words);
		return Arrays.asList(noWhitespaceWords.split(","));	
	}
	
	private String removeWhiteSpace(String words) {
		return words.replaceAll("\\s", "");
	}
}
