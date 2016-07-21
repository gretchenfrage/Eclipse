package com.phoenixkahlo.testing.networking;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.EnumDecoder;
import com.phoenixkahlo.networking.EnumEncoder;

public class EnumCoderTester {

	public static enum TestEnum {
		
		ONE,
		TWO,
		THREE;
		
	}
	
	public static void main(String[] args) {
		EncodingProtocol encoder = new EnumEncoder(TestEnum.class);
		DecodingProtocol decoder = new EnumDecoder(TestEnum.class);
		
	}
	
}
