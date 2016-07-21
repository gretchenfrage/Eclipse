package com.phoenixkahlo.cornsnake;

/**
 * A lexer token. 
 */
public class Token {

	private String contents;
	private TokenType type;
	
	public Token(String contents, TokenType type) {
		this.contents = contents;
		this.type = type;
	}
	
	public String getContents() {
		return contents;
	}
	
	public boolean isA(TokenType type) {
		return this.type == type;
	}
	
	public String toString() {
		return type + ":\"" + contents + "\" ";
	}
	
}
