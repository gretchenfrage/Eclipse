package com.phoenixkahlo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectionUtils {

	private ReflectionUtils() {}

	public static Field[] getAllFields(Class<?> clazz) {
		if (clazz == Object.class)
			return clazz.getDeclaredFields();
		else
			return ArrayUtils.concatenate(
					getAllFields(clazz.getSuperclass()),
					clazz.getDeclaredFields(),
					Field.class);
	}
	
	public static Method[] getAllMethods(Class<?> clazz) {
		if (clazz == Object.class)
			return clazz.getDeclaredMethods();
		else
			return ArrayUtils.concatenate(
					getAllMethods(clazz.getSuperclass()),
					clazz.getDeclaredMethods(),
					Method.class);
	}
	
	public static Field getAnyField(Class<?> clazz, String name) {
		return ArrayUtils.conditionSearch(getAllFields(clazz),
				(Field field) -> field.getName().equals(name));
	}
	
	public static Method getAnyMethod(Class<?> clazz, String name, Class<?>... argTypes) {
		return ArrayUtils.conditionSearch(getAllMethods(clazz),
				(Method method) -> method.getName().equals(name) &&
				ArrayUtils.equals(method.getParameterTypes(), argTypes));
	}
	
}
