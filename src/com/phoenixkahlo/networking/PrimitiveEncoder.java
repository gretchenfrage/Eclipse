package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;

public class PrimitiveEncoder implements EncodingProtocol {

	private Class<?> type;
	
	/**
	 * @param type the class of box, not the primitive class
	 */
	public PrimitiveEncoder(Class<?> type) {
		this.type = type;
	}
		
	@Override
	public boolean canEncode(Object obj) {
		return obj.getClass() == type;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj)) throw new IllegalArgumentException();
		SerializationUtils.writeAny(obj, out, null);
	}
	
}
