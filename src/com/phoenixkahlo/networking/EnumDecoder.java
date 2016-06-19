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
		return clazz.getEnumConstants()[SerializationUtils.readInt(in)];
	}

}
