package com.bot.filter;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.bot.filter.words.HighUrgencyWords;

public class WordsTest {
	
	HighUrgencyWords words;
	
	@Before
	public void setUp() {
		words = new HighUrgencyWords();
		words.setWords(Arrays.asList("fag", "rape"));
		words.setExceptions(Arrays.asList("cofagrigus", "therapeu"));
	}	
	
	@Test
	public void countFlaggedWords() {
		assertEquals(2, words.countFlaggedWords("rapetherapeutic"));
		assertEquals(3, words.countFlaggedWords("rapefagrape"));
	}
	
	@Test
	public void countExceptionsTest() {
		assertEquals(1, words.countExceptions("rapetherapeutic"));
		assertEquals(0, words.countExceptions("rapeabcdfagefghrape"));
	}
	
	@Test
	public void checkWordsTest() {
		assertNull(words.checkWords("therapeutic"));
		assertNull(words.checkWords("cofagrigus"));
		assertNotNull(words.checkWords("rape"));
		assertNotNull(words.checkWords("rapetherpeutic"));
		assertNotNull(words.checkWords("rapeabcdfagefghrape"));
	}
}
