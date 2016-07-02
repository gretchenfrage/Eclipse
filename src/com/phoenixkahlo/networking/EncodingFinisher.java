package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;

public interface EncodingFinisher {

	void finishEncoding(OutputStream out) throws IOException;
	
}
