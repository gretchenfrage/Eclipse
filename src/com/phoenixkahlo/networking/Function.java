package com.phoenixkahlo.networking;

public interface Function {

	void invoke(Object ... args) throws ProtocolViolationException;
	
	Class<?>[] getArgTypes();
	
}
