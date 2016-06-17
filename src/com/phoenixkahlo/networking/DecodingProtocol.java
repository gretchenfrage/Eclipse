package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;

public interface DecodingProtocol {

	Object decode(InputStream in) throws IOException, ProtocolViolationException;
	
}
