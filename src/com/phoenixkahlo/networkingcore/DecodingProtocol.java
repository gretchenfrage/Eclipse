package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;

public interface DecodingProtocol {

	Object decode(InputStream in) throws IOException, ProtocolViolationException;
	
}
