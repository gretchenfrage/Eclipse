package com.phoenixkahlo.testing;

import java.lang.reflect.Field;

public class FrameworkTester {

	public static void main(String[] args) {
		for (Field field : Class.class.getDeclaredFields()) {
			System.out.println(field);
		}
	}

}
