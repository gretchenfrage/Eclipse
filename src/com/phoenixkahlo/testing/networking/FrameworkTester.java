package com.phoenixkahlo.testing.networking;

public class FrameworkTester {

	class Foo {
		
		void baz() {}
		
	}
	
	class Bar extends Foo {
		
		
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(Bar.class.getDeclaredMethod("baz"));
	}

}
