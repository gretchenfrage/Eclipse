package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.phoenixkahlo.utils.ListUtils;

/**
 * Encodes an object by its fields in order of declaration.
 * Begins with boolean to signify if null.
 */
public class FieldEncoder implements EncodingProtocol {

	private Class<?> clazz;
	private EncodingProtocol subEncoder; // Nullable
	
	public FieldEncoder(Class<?> clazz, EncodingProtocol subEncoder) {
		this.clazz = clazz;
		this.subEncoder = subEncoder;
	}
	
	public FieldEncoder(Class<?> clazz) {
		this(clazz, null);
	}
	
	@Override
	public boolean canEncode(Object obj) {
		if (obj == null) return true;
		return obj.getClass() == clazz;
	}
	
	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		SerializationUtils.writeBoolean(obj == null, out);
		if (obj != null)
			encode(obj, out, new ArrayList<Object>());
	}
	
	private void encode(Object obj, OutputStream out, List<Object> encoded) throws IOException,
			IllegalArgumentException {
		if (ListUtils.identityContains(encoded, obj))
			throw new IllegalArgumentException("circular references");
		if (!canEncode(obj))
			throw new IllegalArgumentException("unencodable object");
		encoded.add(obj);
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				if (!Modifier.isTransient(field.getModifiers()))
					SerializationUtils.writeAny(field.get(obj), out, subEncoder);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
}
