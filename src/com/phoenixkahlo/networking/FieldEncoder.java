package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
	
	/**
	 * Encoded classes may annotate methods with this to give them control of their own encoding:
	 * @FieldEncoder.EncodingFinisher 
	 * public void [name](OutputStream out) throws IOException
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface EncodingFinisher {}
	
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
		if (!canEncode(obj)) throw new IllegalArgumentException();
		// Handle null scenario
		SerializationUtils.writeBoolean(obj == null, out);
		if (obj == null)
			return;
		// Handle circular reference scenario
		Thread thread = Thread.currentThread();
		boolean head = false;
		if (!encoded.containsKey(thread)) {
			encoded.put(thread, new ArrayList<Object>());
			head = true;
		}
		if (!head && identityContains(encoded.get(thread), obj))
			throw new IllegalArgumentException("circular references");
		encoded.get(thread).add(obj);
		// Encode fields
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
		// Let object do its own encoding
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(EncodingFinisher.class)) {
				try {
					method.invoke(obj, out);
				} catch (IllegalAccessException e) {
					throw new IllegalStateException(e);
				} catch (InvocationTargetException e) {
					if (e.getTargetException() instanceof IOException)
						throw (IOException) e.getTargetException();
					else
						throw new IllegalStateException(e);
				}
			}
		}
		// Cleanup circular reference handling
		if (head)
			encoded.remove(thread);
	}
	
	private static boolean identityContains(List<?> list, Object obj) {
		for (Object item : list)
			if (item == obj) return true;
		return false;
	}
	
}
