package com.phoenixkahlo.networkingcore;

public interface Function {

	void invoke(Object ... args) throws ProtocolViolationException;
	
	Class<?>[] getArgTypes();
	
}
