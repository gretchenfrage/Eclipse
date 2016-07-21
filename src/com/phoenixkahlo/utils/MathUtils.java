package com.phoenixkahlo.utils;

import java.util.Random;

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
	
}
