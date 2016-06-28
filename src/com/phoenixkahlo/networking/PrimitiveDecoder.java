package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;

public class PrimitiveDecoder implements DecodingProtocol {

	private Class<?> type;
	
	public PrimitiveDecoder(Class<?> type) {
		this.type = type;
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		return SerializationUtils.readType(type, in, null);
	}

}
