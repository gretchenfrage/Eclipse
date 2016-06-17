package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;

public class ArrayEncoder implements EncodingProtocol {

	private Class<?> clazz; // If this encodes int[]s, clazz == int.class
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
		Class<?> objComponentType = obj.getClass().getComponentType();
		// Next line for if obj is primitive array and clazz is primitive class
		if (clazz.isAssignableFrom(objComponentType)) return true;
		// Because Object.class.isAssignableFrom(int.class) == false
		if (objComponentType == short.class)
			objComponentType = Short.class;
		else if (objComponentType == int.class)
			objComponentType = Integer.class;
		else if (objComponentType == long.class)
			objComponentType = Long.class;
		else if (objComponentType == char.class)
			objComponentType = Character.class;
		else if (objComponentType == float.class)
			objComponentType = Float.class;
		else if (objComponentType == double.class)
			objComponentType = Double.class;
		else if (objComponentType == byte.class)
			objComponentType = Byte.class;
		else if (objComponentType == boolean.class)
			objComponentType = Boolean.class;
		return clazz.isAssignableFrom(objComponentType);
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
