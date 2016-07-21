package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UnionDecoder implements DecodingProtocol {

	private Map<Integer, DecodingProtocol> decoders = new HashMap<Integer, DecodingProtocol>();
	
	public void registerProtocol(int header, DecodingProtocol decoder) {
		decoders.put(header, decoder);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		if (SerializationUtils.readBoolean(in))
			return null;
		int header = SerializationUtils.readInt(in);
		DecodingProtocol decoder = decoders.get(header);
		if (decoder == null)
			throw new ProtocolViolationException("header not found: " + header + " on thread " +
					Thread.currentThread());
		return decoder.decode(in);
	}

}
