package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Parses a string.
 */
public class StringParser implements TokenParser {

	@Override
	public boolean isStarted(PushbackReader reader) throws IOException {
		char c = (char) reader.read();
		reader.unread(c);
		return c == '"';
	}

	@Override
	public Token parse(PushbackReader reader) throws IOException, ParseException {
		if ((char) reader.read() != '"')
			throw new ParseException("string must start with '\"'");
		
		StringBuilder builder = new StringBuilder();
		boolean done = false;
		boolean escaped = false;
		while (!done) {
			char c = (char) reader.read();
			if (!escaped && c == '"')
				done = true;
			else if (!escaped && c == '\\')
				escaped = true;
			else
				builder.append(c);
		}
		
		return new Token(builder.toString(), TokenType.STRING);
	}

}
