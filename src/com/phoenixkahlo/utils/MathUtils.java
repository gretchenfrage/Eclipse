package com.phoenixkahlo.utils;

import java.util.Random;
import java.util.function.Function;

public class MathUtils {

	private MathUtils() {}
	
	public static final Random RANDOM = new Random();
	
	public static final float PI_F = (float) Math.PI;
	
	public static float roundDown(float n, float multiple) {
		return n > 0 ? n - n % multiple : n - multiple - n % multiple;
	}
	
	public static double max(double... arr) {
		double out = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] > out)
				out = arr[i];
		}
		return out;
	}

	public static double min(double... arr) {
		double out = arr[0];
		for (int i = 1; i < arr.length; i++) {
			if (arr[i] < out)
				out = arr[i];
		}
		return out;
	}
	
	public static <E> float max(E[] arr, Function<E, Float> map) {
		float out = map.apply(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			float n = map.apply(arr[i]);
			if (n > out)
				out = n;
		}
		return out;
	}
	
	public static <E> float min(E[] arr, Function<E, Float> map) {
		float out = map.apply(arr[0]);
		for (int i = 1; i < arr.length; i++) {
			float n = map.apply(arr[i]);
			if (n < out)
				out = n;
		}
		return out;
	}
	
}