package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;

public interface DecodingFinisher {

	void finishDecoding(InputStream in) throws IOException, ProtocolViolationException;
	
}
