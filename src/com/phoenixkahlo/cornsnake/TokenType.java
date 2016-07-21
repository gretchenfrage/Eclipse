package com.phoenixkahlo.cornsnake;

/**
 * The types of tokens.
 */
public enum TokenType {
	
	LIST_START,
	LIST_END,
	TABLE_START,
	TABLE_END,
	COMMA,
	EQUALS,
	TRUE,
	FALSE,
	VARIABLE,
	NUMBER,
	STRING;
	
	static {
		LIST_START.parser = new CharacterParser('[', LIST_START);
		LIST_END.parser = new CharacterParser(']', LIST_END);
		TABLE_START.parser = new CharacterParser('{', TABLE_START);
		TABLE_END.parser = new CharacterParser('}', TABLE_END);
		COMMA.parser = new CharacterParser(',', COMMA);
		EQUALS.parser = new CharacterParser('=', EQUALS);
		TRUE.parser = new WordParser("true", TRUE);
		FALSE.parser = new WordParser("false", FALSE);
		VARIABLE.parser = new VariableParser();
		NUMBER.parser = new NumberParser();
		STRING.parser = new StringParser();
	}
	
	private TokenParser parser;
	
	public TokenParser getParser() {
		return parser;
	}
	
}
