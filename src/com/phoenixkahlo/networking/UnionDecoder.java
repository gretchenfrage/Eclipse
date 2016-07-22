package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class UnionDecoder implements DecodingProtocol {

	private Map<Integer, DecodingProtocol> decoders = new HashMap<Integer, DecodingProtocol>();
	private UnionEncoder toEncoder; // Should be invalidated (nullified) upon modifications
	
	public void registerProtocol(int header, DecodingProtocol decoder) {
		decoders.put(header, decoder);
		toEncoder = null;
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

	@Override
	public EncodingProtocol toEncoder() {
		if (toEncoder == null) {
			toEncoder = new UnionEncoder();
			for (Map.Entry<Integer, DecodingProtocol> entry : decoders.entrySet()) {
				toEncoder.registerProtocol(entry.getKey(), entry.getValue().toEncoder());
			}
		}
		return toEncoder;
	}

}
