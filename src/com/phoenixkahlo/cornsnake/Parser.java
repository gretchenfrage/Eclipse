package com.phoenixkahlo.cornsnake;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Stack;

/**
 * The static parsing class.
 */
public class Parser {

	private Parser() {}
	
	public static Object parse(String str) throws ParseException {
		try {
			return parse(new StringReader(str));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static Object parse(File file) throws IOException, ParseException {
		return parse(new FileReader(file));
	}
	
	public static Object parse(Reader reader) throws IOException, ParseException {
		return parse(new PushbackReader(reader, 10));
	}
	
	public static Object parse(PushbackReader reader) throws IOException, ParseException {
		// Lexify
		Stack<Token> reverseTokens = new Stack<Token>();		
		while (!isDone(reader)) {
			reverseTokens.push(parseToken(reader));
		}
		Stack<Token> tokens = new Stack<Token>();
		while (!reverseTokens.isEmpty())
			tokens.push(reverseTokens.pop());
		
		// Parse
		Object out = parse(tokens);
		
		if (!tokens.isEmpty())
			throw new ParseException("extra tokens");
		
		return out;
	}
	
	public static Object parse(Stack<Token> tokens) throws ParseException {		
		if (tokens.peek().isA(TokenType.NUMBER)) {
			return Double.parseDouble(tokens.pop().getContents());
		} else if (tokens.peek().isA(TokenType.TRUE)) {
			tokens.pop();
			return true;
		} else if (tokens.peek().isA(TokenType.FALSE)) {
			tokens.pop();
			return false;
		} else if (tokens.peek().isA(TokenType.STRING)) {
			return tokens.pop().getContents();
		} else if (tokens.peek().isA(TokenType.LIST_START)) {
			return new ParseList(tokens);
		} else if (tokens.peek().isA(TokenType.TABLE_START)) {
			return new ParseTable(tokens);
		} else {
			throw new ParseException("object cannot start with " + tokens.peek());
		}
	}
	
	private static Token parseToken(PushbackReader reader) throws IOException, ParseException {
		for (TokenType type : TokenType.values()) {
			if (type.getParser().isStarted(reader))
				return type.getParser().parse(reader);
		}
		throw new ParseException("don't know how to parse token starting with " + (char) reader.read());
	}
	
	/**
	 * Skips whitespace and then tests if this reader yields -1.
	 */
	private static boolean isDone(PushbackReader reader) throws IOException {
		skipWhitespace(reader);
		int n = reader.read();
		reader.unread(n);
		return n == -1 || n == 65535;
	}
	
	public static void skipWhitespace(PushbackReader reader) throws IOException {
		boolean done = false;
		while (!done) {
			char c = (char) reader.read();
			if (c == '#')
				do {
					c = (char) reader.read();
				} while (c != '\n');
			else if (!Character.isWhitespace(c) || c == -1) {
				reader.unread(c);
				done = true;
			}
		}
	}
	
}
