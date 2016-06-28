package com.phoenixkahlo.utils;

import java.lang.reflect.Array;
import java.util.function.Function;

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
	
	/**
	 * @return the first item in arr that satisfies condition, or null if none do.
	 */
	public static <E> E conditionSearch(E[] arr, Function<E, Boolean> condition) {
		for (E e : arr) {
			if (condition.apply(e))
				return e;
		}
		return null;
	}
	
	public static <A, B> boolean equals(A[] aArr, B[] bArr) {
		if (aArr.length != bArr.length) return false;
		for (int i = 0; i < aArr.length; i++) {
			if (!aArr[i].equals(bArr[i])) return false;
		}
		return true;
	}
	
}
