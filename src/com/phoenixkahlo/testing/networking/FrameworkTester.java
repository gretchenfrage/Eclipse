package com.phoenixkahlo.testing.networking;

import java.lang.reflect.Field;

public class FrameworkTester {

	public static void main(String[] args) {
		class Foo {
			@SuppressWarnings("unused")
			int a;
		}
		class Bar extends Foo {
			@SuppressWarnings("unused")
			int b;
		}
		for (Field field : Bar.class.getDeclaredFields()) {
			System.out.println(field);
		}
	}

}
