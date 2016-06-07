package com.phoenixkahlo.testing;

import java.util.function.Supplier;

public class FrameworkTester {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Supplier<Exception> supplier = Exception::new;
	}

}
