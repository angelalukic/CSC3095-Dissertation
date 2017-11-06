package com.bot.filter;

public class Filter {
	
	public String[] splitIntoWordArray(String input) {
		return input.split("\\s+");
	}
	
	public String removeDuplicateChars(String input) {
		
		StringBuilder noDupes = new StringBuilder();
		
		for (int i = 0; i < input.length(); i++) {
	        char letter = input.charAt(i);
	        if(i == 0 || letter != input.charAt(i-1)) {
	        	noDupes.append(letter);
	        }
	    }
	    return noDupes.toString();
	}
}
