package com.phoenixkahlo.testing.networking;

import java.lang.reflect.Field;

public class ArrayFields {

	public static void main(String[] args) {
		for (Field field : byte[].class.getDeclaredFields()) {
			System.out.println(field);
		}
	}
	
}
