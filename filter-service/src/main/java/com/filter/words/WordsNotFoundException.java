package com.filter.words;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(HttpStatus.NOT_FOUND)
public class WordsNotFoundException extends RuntimeException {
	
	public WordsNotFoundException(String message) {
		super(message);
	}
}
