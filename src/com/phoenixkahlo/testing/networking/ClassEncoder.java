package com.phoenixkahlo.testing.networking;

import java.io.IOException;
import java.io.OutputStream;

import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.SerializationUtils;

public class ClassEncoder implements EncodingProtocol {

	@Override
	public boolean canEncode(Object obj) {
		return obj instanceof Class<?>;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj)) throw new IllegalArgumentException();
		Class<?> clazz = (Class<?>) obj;
		SerializationUtils.writeString(clazz.getName(), out);
	}

}
