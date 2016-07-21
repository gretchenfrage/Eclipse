package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Parses a single character. 
 */
public class CharacterParser implements TokenParser {

	private char c;
	private TokenType type;
	
	public CharacterParser(char c, TokenType type) {
		this.c = c;
		this.type = type;
	}

	@Override
	public boolean isStarted(PushbackReader reader) throws IOException {
		char c = (char) reader.read();
		reader.unread(c);
		return c == this.c;
	}

	@Override
	public Token parse(PushbackReader reader) throws IOException, ParseException {
		char c = (char) reader.read();
		if (c == this.c)
			return new Token(Character.toString(c), type);
		else
			throw new ParseException("expected '" + this.c + "' but read '" + c + '\'');
	}
	
}
