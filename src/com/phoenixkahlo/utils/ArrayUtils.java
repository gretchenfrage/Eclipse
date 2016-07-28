package com.phoenixkahlo.utils;

import java.lang.reflect.Array;
import java.util.function.Predicate;

public class ArrayUtils {

	private ArrayUtils() {}
	
	public static <E> E[] concatenate(E[] arr1, E[] arr2) {
		@SuppressWarnings("unchecked")
		E[] out = (E[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + arr2.length);
		for (int i = 0; i < arr1.length; i++) {
			out[i] = arr1[i];
		}
		for (int i = 0; i < arr2.length; i++) {
			out[arr1.length + i] = arr2[i];
		}
		return out;
	}
	
	public static <E> E[] append(E[] arr1, E obj) {
		@SuppressWarnings("unchecked")
		E[] out = (E[]) Array.newInstance(arr1.getClass().getComponentType(), arr1.length + 1);
		for (int i = 0; i < arr1.length; i++) {
			out[i] = arr1[i];
		}
		out[out.length - 1] = obj;
		return out;
	}
	
	/**
	 * @return the first item in arr that satisfies condition, or null if none do.
	 */
	public static <E> E conditionSearch(E[] arr, Predicate<E> condition) {
		for (E e : arr) {
			if (condition.test(e))
				return e;
		}
		return null;
	}
	
	public static boolean equals(Object[] aArr, Object[] bArr) {
		if (aArr.length != bArr.length) return false;
		for (int i = 0; i < aArr.length; i++) {
			if (!aArr[i].equals(bArr[i])) return false;
		}
		return true;
	}
	
	public static <E> E[] copy(E[] arr) {
		@SuppressWarnings("unchecked")
		E[] copy = (E[]) Array.newInstance(arr.getClass().getComponentType(), arr.length);
		for (int i = 0; i < arr.length; i++) {
			copy[i] = arr[i];
		}
		return copy;
	}
	
}
