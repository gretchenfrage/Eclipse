package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.phoenixkahlo.utils.ReflectionUtils;

/**
 * Decodes an object by its fields in order of declaration.
 * Begins with boolean to signify if null.
 */
public class FieldDecoder implements DecodingProtocol {
	
	private final Supplier<?> supplier;
	private final Field[] fields;
	private final DecodingProtocol subDecoder; // Nullable
	private final Predicate<Field> condition;
	
	public <E> FieldDecoder(Class<E> clazz, Supplier<E> supplier, DecodingProtocol subDecoder, 
			Predicate<Field> condition) {
		this.supplier = supplier;
		this.subDecoder = subDecoder;
		this.condition = condition; 
		fields = ReflectionUtils.getAllFields(clazz);
		for (Field field : fields)
			field.setAccessible(true);
	}
	
	public <E> FieldDecoder(Class<E> clazz, Supplier<E> supplier, DecodingProtocol subDecoder) {
		this(clazz, supplier, subDecoder, (Field field) -> !Modifier.isTransient(field.getModifiers()) && 
				!Modifier.isStatic(field.getModifiers()));
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
			// Decode fields
			for (Field field : fields) {
				try {
					if (condition.test(field)) {
						field.set(obj, SerializationUtils.readType(field.getType(), in, subDecoder));
					}
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			// Allow DecodingFinishers to finish
			if (obj instanceof DecodingFinisher) {
				((DecodingFinisher) obj).finishDecoding(in);
			}
			return obj;
		}
	}
	
}
