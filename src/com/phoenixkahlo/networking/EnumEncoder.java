package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;

public class EnumEncoder implements EncodingProtocol {

	private Class<?> clazz;

	public EnumEncoder(Class<? extends Enum<?>> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public boolean canEncode(Object obj) {
		return obj.getClass() == clazz;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj))
			throw new IllegalArgumentException();
		SerializationUtils.writeInt(((Enum<?>) obj).ordinal(), out);
	}
	
}
