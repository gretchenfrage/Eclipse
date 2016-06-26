package com.phoenixkahlo.testing.networking;

import com.phoenixkahlo.networking.SerializationUtils;

public class FrameworkTester {

	public static void main(String[] args) {
		System.out.println(SerializationUtils.bytesToInt(new byte[] {0, 0, 0, 0}));
		System.out.println(SerializationUtils.bytesToInt(new byte[] {-1, -1, -1, -1}));
	}

}
