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
		DecodingProtocol decoder = decoders.get(SerializationUtils.readInt(in));
		if (decoder == null) throw new ProtocolViolationException();
		return decoder.decode(in);
	}

}
