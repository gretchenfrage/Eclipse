package com.phoenixkahlo.testing.networking;

import java.util.function.Function;

public class FrameworkTester {

	FrameworkTester(String str) {}
	
	public static void main(String[] args) throws Exception {
		@SuppressWarnings("unused")
		Function<String, FrameworkTester> function = FrameworkTester::new;
	}

}
