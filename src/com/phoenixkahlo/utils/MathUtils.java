package com.phoenixkahlo.utils;

public class MathUtils {

	private MathUtils() {}
	
	public static float roundDown(float n, float multiple) {
		return n > 0 ? n - n % multiple : n - multiple - n % multiple;
	}
	
}
