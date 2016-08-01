package com.phoenixkahlo.utils;

import java.lang.reflect.Array;
import java.util.function.Function;
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
	
	public static boolean contains(Object[] arr, Object obj) {
		for (Object item : arr) {
			if (item.equals(obj))
				return true;
		}
		return false;
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
	
	public static <A, B> B[] map(A[] arr, Function<A, B> function, Class<B> clazz) {
		@SuppressWarnings("unchecked")
		B[] out = (B[]) Array.newInstance(clazz, arr.length);
		for (int i = 0; i < out.length; i++) {
			out[i] = function.apply(arr[i]);
		}
		return out;
	}
	
	public static <E> E maxProperty(E[] arr, Function<E, Double> property) {
		E greatestItem = arr[0];
		double greatestValue = property.apply(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			double value = property.apply(arr[i]);
			if (value > greatestValue) {
				greatestItem = arr[i];
				greatestValue = value;
			}
		}
		return greatestItem;
	}
	
	public static <E> E minProperty(E[] arr, Function<E, Double> property) {
		E greatestItem = arr[0];
		double greatestValue = property.apply(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			double value = property.apply(arr[i]);
			if (value < greatestValue) {
				greatestItem = arr[i];
				greatestValue = value;
			}
		}
		return greatestItem;
	}
	
	public static <E> E maxProperty(Function<Integer, E> getter, Function<E, Double> property, int length) {
		E greatestItem = getter.apply(0);
		double greatestValue = property.apply(greatestItem);
		for (int i = 1; i < length; i++) {
			E item = getter.apply(i);
			double value = property.apply(item);
			if (value > greatestValue) {
				greatestItem = item;
				greatestValue = value;
			}
		}
		return greatestItem;
	}
	
	public static <E> E minProperty(Function<Integer, E> getter, Function<E, Double> property, int length) {
		E greatestItem = getter.apply(0);
		double greatestValue = property.apply(greatestItem);
		for (int i = 1; i < length; i++) {
			E item = getter.apply(i);
			double value = property.apply(item);
			if (value < greatestValue) {
				greatestItem = item;
				greatestValue = value;
			}
		}
		return greatestItem;
	}
	
}
