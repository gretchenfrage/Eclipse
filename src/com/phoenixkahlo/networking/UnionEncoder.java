package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class UnionEncoder implements EncodingProtocol {

	private Map<EncodingProtocol, Integer> encoders = new HashMap<EncodingProtocol, Integer>();
	private UnionDecoder toDecoder; // Should be invalidated (nullified) upon modifications
	
	public void registerProtocol(int header, EncodingProtocol encoder) {
		encoders.put(encoder, header);
		toDecoder = null;
	}

	@Override
	public boolean canEncode(Object obj) {
		if (obj == null)
			return true;
		for (EncodingProtocol encoder : encoders.keySet()) {
			if (encoder.canEncode(obj)) return true;
		}
		return false;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj))
			throw new IllegalArgumentException("unencodable object: " + obj);
		SerializationUtils.writeBoolean(obj == null, out);
		if (obj == null)
			return;
		for (EncodingProtocol encoder : encoders.keySet()) {
			if (encoder.canEncode(obj)) {
				SerializationUtils.writeInt(encoders.get(encoder), out);
				encoder.encode(obj, out);
				return;
			}
		}
		throw new IllegalArgumentException();
	}

	@Override
	public DecodingProtocol toDecoder() {
		if (toDecoder == null) {
			toDecoder = new UnionDecoder();
			for (Map.Entry<EncodingProtocol, Integer> entry : encoders.entrySet()) {
				toDecoder.registerProtocol(entry.getValue(), entry.getKey().toDecoder());
			}
		}
		return toDecoder;
	}
	
}
