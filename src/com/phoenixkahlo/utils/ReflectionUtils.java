package com.phoenixkahlo.utils;

import java.lang.reflect.Field;

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
	
}
