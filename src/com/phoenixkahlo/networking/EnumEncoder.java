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
		int length = clazz.getEnumConstants().length;
		if (length <= 256)
			out.write(((Enum<?>) obj).ordinal());
		else if (length <= Short.MAX_VALUE)
			SerializationUtils.writeShort((short) ((Enum<?>) obj).ordinal(), out);
		else
			SerializationUtils.writeInt(((Enum<?>) obj).ordinal(), out);
	}
	
}
