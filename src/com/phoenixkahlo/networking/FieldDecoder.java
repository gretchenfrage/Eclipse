package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Supplier;

import com.phoenixkahlo.utils.ReflectionUtils;

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
		fields = ReflectionUtils.getAllFields(clazz);//clazz.getDeclaredFields();
		for (Field field : fields)
			field.setAccessible(true);
		/*
		decodingFinishers = new ArrayList<Method>();
		for (Method method : clazz.getMethods()) {
			if (method.isAnnotationPresent(DecodingFinisher.class))
				decodingFinishers.add(method);
		}
		*/
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
			if (obj instanceof DecodingFinisher) {
				((DecodingFinisher) obj).finishDecoding(in);
			}
			/*
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
			*/
			return obj;
		}
	}
	
}
