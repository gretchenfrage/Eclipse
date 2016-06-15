package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;

public class PrimitiveDecoder implements DecodingProtocol {

	private Class<?> type;
	
	/**
	 * @param type the class of box, not the primitive class
	 */
	public PrimitiveDecoder(Class<Number> type) {
		this.type = type;
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		return SerializationUtils.readType(type, in, null);
	}

}
