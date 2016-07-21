package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;

public class EnumDecoder implements DecodingProtocol {

	private Class<?> clazz;
	
	public EnumDecoder(Class<? extends Enum<?>> clazz) {
		this.clazz = clazz;
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		int index;
		int length = clazz.getEnumConstants().length;
		if (length <= 256)
			index = in.read();
		else if (length <= Short.MAX_VALUE)
			index = SerializationUtils.readShort(in);
		else
			index = SerializationUtils.readInt(in);
		return clazz.getEnumConstants()[index];
	}

}
