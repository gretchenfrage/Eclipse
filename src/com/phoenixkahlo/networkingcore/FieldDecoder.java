package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

/**
 * Decodes an object by its fields in order of declaration.
 * Begins with boolean to signify if null.
 */
public class FieldDecoder implements DecodingProtocol {

	private Supplier<?> supplier;
	private Field[] fields;
	private DecodingProtocol subDecoder; // Nullable
	
	public <E> FieldDecoder(Class<E> clazz, Supplier<E> supplier, DecodingProtocol subDecoder) {
		this.supplier = supplier;
		this.subDecoder = subDecoder;
		fields = clazz.getDeclaredFields();
		for (Field field : fields)
			field.setAccessible(true);
	}
	
	public <E> FieldDecoder(Class<E> clazz, Supplier<E> supplier) {
		this(clazz, supplier, null);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		if (SerializationUtils.readBoolean(in))
			return null;
		else {
			Object obj = supplier.get();
			for (Field field : fields) {
				try {
					int mods = field.getModifiers();
					if (!Modifier.isTransient(mods) && !Modifier.isStatic(mods))
						field.set(obj, SerializationUtils.readType(field.getType(), in, subDecoder));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			return obj;
		}
	}
	
}
