package com.phoenixkahlo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ReflectionUtils {

	private ReflectionUtils() {}

	/**
	 * @return all of clazz's fields, including invisible fields and superclass fields.
	 */
	public static Field[] getAllFields(Class<?> clazz) {
		if (clazz == Object.class)
			return clazz.getDeclaredFields();
		else
			return ArrayUtils.concatenate(
					getAllFields(clazz.getSuperclass()),
					clazz.getDeclaredFields(),
					Field.class);
	}
	
	/**
	 * @return all of clazz's methods, including invisible methods and superclass methods.
	 */
	public static Method[] getAllMethods(Class<?> clazz) {
		if (clazz == Object.class)
			return clazz.getDeclaredMethods();
		else
			return ArrayUtils.concatenate(
					getAllMethods(clazz.getSuperclass()),
					clazz.getDeclaredMethods(),
					Method.class);
	}
	
	/**
	 * @return clazz's field with that name, even if it is invisible or of a superclass.
	 */
	public static Field getAnyField(Class<?> clazz, String name) {
		return ArrayUtils.conditionSearch(getAllFields(clazz),
				field -> field.getName().equals(name));
	}
	
	/**
	 * @return clazz's method with that name and argument types, even if it is invisible or of a superclass.
	 */
	public static Method getAnyMethod(Class<?> clazz, String name, Class<?>... argTypes) {
		return ArrayUtils.conditionSearch(getAllMethods(clazz),
				method -> method.getName().equals(name) &&
				ArrayUtils.equals(method.getParameterTypes(), argTypes));
	}
	
	/**
	 * Sets the value of a public static final variable.
	 */
	public static void setConstant(Class<?> clazz, String name, Object value) throws IllegalArgumentException {
		try {
			Field field = clazz.getField(name);
			Field modifiers = Field.class.getDeclaredField("modifiers");
			modifiers.setAccessible(true);
			modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, value);
		} catch (NoSuchFieldException | IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
}
