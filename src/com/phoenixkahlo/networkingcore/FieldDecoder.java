package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Decodes an object by its fields in order of declaration.
 * Begins with boolean to signify if null.
 */
public class FieldDecoder implements DecodingProtocol {

	/**
	 * Decoded classes may annotate methods with this to give them control of their own decoding:
	 * @FieldDecoder.DecodingFinisher 
	 * public void [name](InputStream out) throws IOException, ProtocolViolationException
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public static @interface DecodingFinisher {}
	
	private Supplier<?> supplier;
	private Field[] fields;
	private List<Method> decodingFinishers;
	private DecodingProtocol subDecoder; // Nullable
	
	public <E> FieldDecoder(Class<E> clazz, Supplier<E> supplier, DecodingProtocol subDecoder) {
		this.supplier = supplier;
		this.subDecoder = subDecoder;
		fields = clazz.getDeclaredFields();
		for (Field field : fields)
			field.setAccessible(true);
		decodingFinishers = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(DecodingFinisher.class))
				decodingFinishers.add(method);
		}
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
					int mods = field.getModifiers();
					if (!Modifier.isTransient(mods) && !Modifier.isStatic(mods))
						field.set(obj, SerializationUtils.readType(field.getType(), in, subDecoder));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					throw new RuntimeException(e);
				}
			}
			// Allow DecodingFinishers to finish
			for (Method method : decodingFinishers) {
				try {
					method.invoke(obj, in);
				} catch (IllegalAccessException | IllegalArgumentException e) {
					throw new IllegalStateException(e);
				} catch (InvocationTargetException e) {
					if (e.getTargetException() instanceof IOException)
						throw (IOException) e.getTargetException();
					else if (e.getTargetException() instanceof ProtocolViolationException)
						throw (ProtocolViolationException) e.getTargetException();
					else
						throw new IllegalStateException(e);
				}
			}
			return obj;
		}
	}
	
}
