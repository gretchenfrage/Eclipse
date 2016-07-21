package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;

public class HashMapEncoder implements EncodingProtocol {

	private EncodingProtocol keyArrayEncoder;
	private EncodingProtocol valueArrayEncoder;
	
	public HashMapEncoder(EncodingProtocol keyItemEncoder, EncodingProtocol valueItemEncoder) {
		keyArrayEncoder = new ArrayEncoder(Object.class, keyItemEncoder);
		valueArrayEncoder = new ArrayEncoder(Object.class, valueItemEncoder);
	}
	
	@Override
	public boolean canEncode(Object obj) {
		if (obj == null)
			return true;
		return obj instanceof HashMap<?, ?>;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj)) throw new IllegalArgumentException();
		SerializationUtils.writeBoolean(obj == null, out);
		if (obj == null)
			return;
		HashMap<?, ?> map = (HashMap<?, ?>) obj;
		Object[] keys = map.keySet().toArray();
		Object[] values = map.values().toArray();
		keyArrayEncoder.encode(keys, out);
		valueArrayEncoder.encode(values, out);
	}

}
