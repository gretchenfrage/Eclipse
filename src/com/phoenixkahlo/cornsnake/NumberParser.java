package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * Parses a number
 */
public class NumberParser implements TokenParser {
	
	@Override
	public boolean isStarted(PushbackReader reader) throws IOException {
		char peek = (char) reader.read();
		reader.unread(peek);
		return Character.isDigit(peek) || peek == '.' || peek == '-';
	}

	@Override
	public Token parse(PushbackReader reader) throws IOException, ParseException {
		if (!isStarted(reader))
			throw new ParseException("illegal start of number: '" + reader.read() + '\'');
		
		StringBuilder builder = new StringBuilder();
		boolean done = false;
		while (!done) {
			char c = (char) reader.read();
			if (c == '-' || c == '.' || isLegalDouble(builder.toString() + c)) {
				builder.append(c);
			} else {
				reader.unread(c);
				done = true;
			}
		}
		return new Token(builder.toString(), TokenType.NUMBER);
	}
	
	/**
	 * Evil method incorporating exception logic into regular program flow.
	 */
	private static boolean isLegalDouble(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}
