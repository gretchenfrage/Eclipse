package com.phoenixkahlo.testing;

import java.lang.reflect.Field;

public class TestUtils {

	public static Object get(Object obj, String name) {
		try {
			Field field = obj.getClass().getDeclaredField(name);
			field.setAccessible(true);
			return field.get(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
