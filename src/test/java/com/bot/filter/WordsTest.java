package com.bot.filter;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

public class WordsTest {
	
	HighUrgencyWords words;
	
	@Before
	public void setUp() {
		words = new HighUrgencyWords();
		words.setWords(Arrays.asList("fag", "pedo"));
		words.setExceptions(Arrays.asList("cofagrigus"));
	}	

	@Test
	public void checkExceptionsTest() {	
		assertNull(words.checkExceptions("cofagrigus"));
		assertEquals("fag", words.checkExceptions("fag"));
		assertEquals("pedo", words.checkExceptions("pedo"));	
		assertNull(words.checkExceptions("cofagrigus,"));
	}
}
