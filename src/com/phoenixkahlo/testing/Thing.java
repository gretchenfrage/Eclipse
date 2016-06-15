package com.phoenixkahlo.testing;

import com.phoenixkahlo.networkingcore.ArrayEncoder;

public class Thing {

	public static void main(String[] args) {
		ArrayEncoder encoder = new ArrayEncoder(Object.class);
		Object[] arr = new String[5];
		System.out.println(encoder.canEncode(arr));
	}

}
