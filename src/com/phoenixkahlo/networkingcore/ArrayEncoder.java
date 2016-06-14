package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;

public class ArrayEncoder implements EncodingProtocol {

	private Class<?> clazz;
	private EncodingProtocol itemEncoder; // Nullable
	
	public ArrayEncoder(Class<?> clazz, EncodingProtocol itemEncoder) {
		this.clazz = clazz;
		this.itemEncoder = itemEncoder;
	}
	
	public ArrayEncoder(Class<?> clazz) throws IllegalArgumentException {
		this(clazz, null);
	}
	
	@Override
	public boolean canEncode(Object obj) {
		if (!obj.getClass().isArray()) return false;
		return obj.getClass().getComponentType() == clazz;
	}

	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		if (!canEncode(obj)) throw new IllegalArgumentException();
		int length = Array.getLength(obj);
		SerializationUtils.writeInt(length, out);
		for (int i = 0; i < length; i++) {
			SerializationUtils.writeAny(Array.get(obj, i), out, itemEncoder);
		}
	}

}
