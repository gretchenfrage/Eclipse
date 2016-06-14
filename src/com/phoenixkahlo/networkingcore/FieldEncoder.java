package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	// Protects against FieldEncoders getting into infinite loops with circular references
	private static Map<Thread, List<Object>> encoded = new HashMap<Thread, List<Object>>();
	
	@Override
	public void encode(Object obj, OutputStream out) throws IOException, IllegalArgumentException {
		SerializationUtils.writeBoolean(obj == null, out);
		if (obj == null)
			return;
		if (!canEncode(obj)) throw new IllegalArgumentException();
		Thread thread = Thread.currentThread();
		boolean head = false;
		if (!encoded.containsKey(thread)) {
			encoded.put(thread, new ArrayList<Object>());
			head = true;
		}
		if (!head && identityContains(encoded.get(thread), obj))
			throw new IllegalArgumentException("circular references");
		encoded.get(thread).add(obj);
		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			try {
				int mods = field.getModifiers();
				if (!Modifier.isTransient(mods) && !Modifier.isStatic(mods))
					SerializationUtils.writeAny(field.get(obj), out, subEncoder);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		if (head)
			encoded.remove(thread);
	}
	
	private static boolean identityContains(List<?> list, Object obj) {
		for (Object item : list)
			if (item == obj) return true;
		return false;
	}
	
}
