package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.OutputStream;

public interface EncodingProtocol {

	boolean canEncode(Object obj);
	
	void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException;
	
}
