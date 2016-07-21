package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Parses a single word. 
 */
public class WordParser implements TokenParser {

	private String word;
	private TokenType type;
	
	public WordParser(String word, TokenType type) {
		this.word = word;
		this.type = type;
	}

	@Override
	public boolean isStarted(PushbackReader reader) throws IOException {
		char[] buffer = new char[word.length()];
		reader.read(buffer);
		reader.unread(buffer);
		return new String(buffer).equals(word);
	}

	@Override
	public Token parse(PushbackReader reader) throws IOException, ParseException {
		char[] buffer = new char[word.length()];
		reader.read(buffer);
		if (new String(buffer).equals(word))
			return new Token(word, type);
		else
			throw new ParseException(new String(buffer) + " doesn't match " + word);
	}
	
}
