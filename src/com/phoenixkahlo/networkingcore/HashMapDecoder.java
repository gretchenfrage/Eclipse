package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HashMapDecoder implements DecodingProtocol {

	private DecodingProtocol keyArrayDecoder;
	private DecodingProtocol valueArrayDecoder;
	
	public HashMapDecoder(DecodingProtocol keyItemDecoder, DecodingProtocol valueItemDecoder) {
		keyArrayDecoder = new ArrayDecoder(Object.class, keyItemDecoder);
		valueArrayDecoder = new ArrayDecoder(Object.class, valueItemDecoder);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException,
			ProtocolViolationException {
		Object[] keys = (Object[]) keyArrayDecoder.decode(in);
		Object[] values = (Object[]) valueArrayDecoder.decode(in);
		Map<Object, Object> map = new HashMap<Object, Object>();
		for (int i = 0; i < keys.length; i++) {
			map.put(keys[i], values[i]);
		}
		return map;
	}

}
