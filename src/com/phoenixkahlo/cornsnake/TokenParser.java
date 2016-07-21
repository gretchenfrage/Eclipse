package com.phoenixkahlo.cornsnake;

import java.io.IOException;
import java.io.PushbackReader;

/**
 * An instance of TokenTypeParser will parse a type of token.
 */
public interface TokenParser {

	boolean isStarted(PushbackReader reader) throws IOException;
	
	Token parse(PushbackReader reader) throws IOException, ParseException;
	
}
