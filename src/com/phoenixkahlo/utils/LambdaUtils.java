package com.phoenixkahlo.utils;

import java.util.Comparator;
import java.util.function.Function;

public class LambdaUtils {

	private LambdaUtils() {}
	
	public static <E> Comparator<E> compare(Function<E, Double> property) {
		return new Comparator<E>() {

			@Override
			public int compare(E e1, E e2) {
				double diff = property.apply(e2) - property.apply(e1);
				return diff > 0 ? 1 : -1;
			}
			
		};
	}
	
}
