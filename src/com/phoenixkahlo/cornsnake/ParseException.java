package com.phoenixkahlo.cornsnake;

public class ParseException extends Exception {

	private static final long serialVersionUID = -3388740683421120489L;

	// No explanation-less exceptions for you!
	
	public ParseException(String cause) {
		super(cause);
	}

	public ParseException(Exception cause) {
		super(cause);
	}

}