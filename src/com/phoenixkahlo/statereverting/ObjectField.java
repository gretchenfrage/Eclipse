package com.phoenixkahlo.statereverting;

import java.lang.reflect.Field;

/**
 * Represents a field and an object. May represent static fields and invisible fields.
 */
public class ObjectField {

	private Field field;
	private Object object; // Nullable to represent static fields
	
	public ObjectField(Field field, Object object) {
		this.field = field;
		this.object = object;
	}
	
	public ObjectField(Object object, String name) throws IllegalArgumentException {
		this.object = object;
		try {
			field = object.getClass().getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public ObjectField(Class<?> clazz, String name) throws IllegalArgumentException {
		try {
			field = clazz.getDeclaredField(name);
		} catch (NoSuchFieldException | SecurityException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	public void breakSecurity() {
		field.setAccessible(true);
	}
	
	public Object get() {
		try {
			return field.get(object);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void set(Object value) throws IllegalArgumentException {
		try {
			field.set(object, value);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}
	
}
