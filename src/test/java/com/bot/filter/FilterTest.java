package com.bot.filter;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FilterTest {
	
	Filter filter;
	
	@Before
	public void setUp() {
		filter = new Filter();
	}

	@Test
	public void removeDuplicateCharsTest() {
		assertEquals("abcd", filter.removeDuplicateChars("aabbccdd"));
		assertEquals("abcd", filter.removeDuplicateChars("abcd"));
		assertEquals("a", filter.removeDuplicateChars("a"));
		assertEquals("", filter.removeDuplicateChars(""));
	}
}
