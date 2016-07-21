package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Parses a variable name.
 */
public class VariableParser implements TokenParser {
	
	@Override
	public boolean isStarted(PushbackReader reader) throws IOException {
		char peek = (char) reader.read();
		reader.unread(peek);
		return Character.isAlphabetic(peek) || peek == '_';
	}

	@Override
	public Token parse(PushbackReader reader) throws IOException, ParseException {
		if (!isStarted(reader))
			throw new ParseException("illegal start of variable name: '" + reader.read() + '\'');
		
		StringBuilder builder = new StringBuilder();
		boolean done = false;
		while (!done) {
			char c = (char) reader.read();
			if (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_')
				builder.append(c);
			else {
				reader.unread(c);
				done = true;
			}
		}
		
		return new Token(builder.toString(), TokenType.VARIABLE);
	}
	
}
