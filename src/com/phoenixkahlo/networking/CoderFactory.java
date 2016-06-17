package com.phoenixkahlo.networking;

public class CoderFactory {

	private CoderFactory() {}
	
	public static EncodingProtocol makeStringEncoder() {
		return new FieldEncoder(String.class, new ArrayEncoder(char.class));
	}
	
	public static DecodingProtocol makeStringDecoder() {
		return new FieldDecoder(String.class, String::new, new ArrayDecoder(char.class));
	}
	
}