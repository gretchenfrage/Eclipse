package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Works for all ArrayList types.
 */
public class ArrayListEncoder implements EncodingProtocol {

	private EncodingProtocol arrayEncoder;
	
	public ArrayListEncoder(EncodingProtocol itemEncoder) {
		arrayEncoder = new ArrayEncoder(Object.class, itemEncoder);
	}
	
	@Override
	public boolean canEncode(Object obj) {
		return obj instanceof ArrayList<?>;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj)) throw new IllegalArgumentException();
		ArrayList<?> list = (ArrayList<?>) obj;
		arrayEncoder.encode(list.toArray(), out);
	}

}
