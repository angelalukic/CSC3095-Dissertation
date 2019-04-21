package com.filter.message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.filter.words.Word;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MessageUtils {
	
	public MessageJudgement getAssessment(String message, List<Word> words) {
		MessageJudgement rawJudgement = assess(message, words);
		MessageJudgement sanitizedJudgement = assess(sanitizeInput(message), words);
		MessageJudgement sanitizedWordsJudgement = assess(message, sanitizeWords(words));
		
		if(rawJudgement.getJudgement() == JudgementLevel.RED || sanitizedWordsJudgement.getJudgement() == JudgementLevel.RED)
			return rawJudgement;
		// rawJudgement is ORANGE or GREEN, sanitizedWordsJudgement is ORANGE OR GREEN
		if (sanitizedJudgement.getJudgement() == JudgementLevel.RED) {
			sanitizedJudgement.setJudgement(JudgementLevel.YELLOW);
			return sanitizedJudgement;
		}
		else if (sanitizedJudgement.getJudgement() == JudgementLevel.YELLOW)
			return sanitizedJudgement;
		else if (sanitizedWordsJudgement.getJudgement() == JudgementLevel.YELLOW) 
			return sanitizedWordsJudgement;
		return rawJudgement;
	}

	public MessageJudgement assess(String message, List<Word> words) {
		int totalBlacklisted = 0;
		int totalGreylisted = 0;
		int totalWhitelisted = 0;
		MessageJudgement judgement = new MessageJudgement(Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), JudgementLevel.GREEN);
		for(int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			int matches = countMatches(message.toLowerCase(), word.getWord().toLowerCase());
			if(word.getLevel() == JudgementLevel.RED && matches > 0) {
				log.debug(matches + " Matches on Blacklisted Word: " + word.getWord());
				totalBlacklisted += matches;
				addToBlacklist(judgement, word);
			}
			else if(word.getLevel() == JudgementLevel.YELLOW && matches > 0) {
				log.debug(matches + " Matches on Greylisted Word: " + word.getWord());
				totalGreylisted += matches;
				addToGreylist(judgement, word);
			}
			else if(word.getLevel() == JudgementLevel.GREEN && matches > 0) {
				log.debug(matches + " Matches on Whitelisted Word: " + word.getWord());
				totalWhitelisted += matches;
				addToWhitelist(judgement, word);
			}
		}
		return getFinalJudgement(totalBlacklisted, totalGreylisted, totalWhitelisted, judgement);
	}
	
	private int countMatches(String message, String word) {
		Matcher m = Pattern.compile(word).matcher(message);
		int matches = 0;
		while(m.find())
			matches++;
		return matches;
	}

	private void addToBlacklist(MessageJudgement judgement, Word word) {
		List<String> blacklist = new ArrayList<>(judgement.getBlacklist());
		blacklist.add(word.getWord());
		judgement.setBlacklist(blacklist);
	}
	
	private void addToGreylist(MessageJudgement judgement, Word word) {
		List<String> greylist = new ArrayList<>(judgement.getGreylist());
		greylist.add(word.getWord());
		judgement.setGreylist(greylist);
	}
	
	private void addToWhitelist(MessageJudgement judgement, Word word) {
		List<String> whitelist = new ArrayList<>(judgement.getWhitelist());
		whitelist.add(word.getWord());
		judgement.setWhitelist(whitelist);
	}
	
	private MessageJudgement getFinalJudgement(int totalBlacklisted, int totalGreylisted, int totalWhitelisted,	MessageJudgement judgement) {
		if(totalBlacklisted > 0) {
			if(totalWhitelisted > 0)
				judgement.setJudgement(JudgementLevel.YELLOW);
			else
				judgement.setJudgement(JudgementLevel.RED);
		}
		else if(totalGreylisted > 0) {
			judgement.setJudgement(JudgementLevel.YELLOW);
		}
		log.debug("Judgement: " + judgement.getJudgement().toString());
		return judgement;
	}
	
	private String sanitizeInput(String message) {
		log.debug("Message Before Sanitation: " + message);
		StringBuilder builder = new StringBuilder(message);
		for(int i = 0; i < builder.length(); i++) {
			char currentChar = builder.charAt(i);
			switch(currentChar) {
				case '0':
					builder.setCharAt(i, 'o');
					break;
				case '1':
					builder.setCharAt(i, 'l');
					break;
				case '2':
					builder.setCharAt(i, 'z');
					break;
				case '3':
					builder.setCharAt(i, 'e');
					break;
				case '4':
					builder.setCharAt(i, 'h');
					break;
				case '5':
					builder.setCharAt(i, 's');
					break;
				case '6':
					builder.setCharAt(i, 'b');
					break;
				case '7':
					builder.setCharAt(i, 't');
					break;
				case '8':
					builder.setCharAt(i, 'b');
					break;
				case '9':
					builder.setCharAt(i, 'g');
					break;
				case '!':
					builder.setCharAt(i, 'i');
					break;
				case '@':
					builder.setCharAt(i, 'a');
					break;
				case '$':
					builder.setCharAt(i, 's');
					break;
				default:
					break;
			}
		}
		String sanitizedMessage = builder.toString().replaceAll("/[^A-Za-z]/g", "");
		log.debug("Message After Sanitation: " + sanitizedMessage);
		return sanitizedMessage;
	}
	
	private List<Word> sanitizeWords(List<Word> words) {
		List<Word> sanitizedWords = new ArrayList<>();
		for(int i = 0; i < words.size(); i++) {
			Word word = words.get(i);
			log.debug("Word Before Sanitation: " + word.getWord());
			String reformedWord = replaceAll(word.getWord(), "\\*");
			log.debug("Word Aftger Sanitation: " + reformedWord);
			sanitizedWords.add(new Word(reformedWord, word.getLevel()));
		}
		return sanitizedWords;
	}
	
	private String replaceAll(String word, String replacer) {
		StringBuilder ret = new StringBuilder();
		if(word.length()>2) {
			ret.append(word.charAt(0));
			for(int j = 1; j < word.length() - 1; j++)
				ret.append(replacer);
			ret.append(word.charAt(word.length() - 1));
			return ret.toString();
		}
		return word;
	}
}
