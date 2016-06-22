package com.phoenixkahlo.utils;

import java.lang.reflect.Array;

public class ArrayUtils {

	private ArrayUtils() {}
	
	public static <E> E[] concatenate(E[] arr1, E[] arr2, Class<E> clazz) {
		@SuppressWarnings("unchecked")
		E[] out = (E[]) Array.newInstance(clazz, arr1.length + arr2.length);
		for (int i = 0; i < arr1.length; i++) {
			out[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			out[arr1.length + i] = arr2[i];
		}
		return out;
	}
	
}
