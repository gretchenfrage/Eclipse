package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class ArrayDecoder implements DecodingProtocol {

	private Class<?> clazz;
	private DecodingProtocol itemDecoder; // Nullable
	
	public ArrayDecoder(Class<?> clazz, DecodingProtocol itemDecoder) {
		this.clazz = clazz;
		this.itemDecoder = itemDecoder;
	}
	
	public ArrayDecoder(Class<?> clazz) throws IllegalArgumentException {
		this(clazz, null);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		int length = SerializationUtils.readInt(in);
		Object arr = Array.newInstance(clazz, length);
		for (int i = 0; i < length; i++) {
			Array.set(arr, i, SerializationUtils.readType(clazz, in, itemDecoder));
		}
		return arr;
	}

}