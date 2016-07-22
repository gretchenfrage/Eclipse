package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.EnumDecoder;
import com.phoenixkahlo.networking.EnumEncoder;
import com.phoenixkahlo.networking.ProtocolViolationException;

public class EnumCoderTester {

	public static enum TestEnum {
		
		ONE,
		TWO,
		THREE;
		
	}
	
	public static void main(String[] args) throws IOException, ProtocolViolationException {
		EncodingProtocol encoder = new EnumEncoder(TestEnum.class);
		DecodingProtocol decoder = new EnumDecoder(TestEnum.class);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(TestEnum.ONE, out);
		encoder.encode(TestEnum.TWO, out);
		encoder.encode(TestEnum.THREE, out);
		encoder.encode(TestEnum.THREE, out);
		encoder.encode(TestEnum.ONE, out);
		encoder.encode(TestEnum.TWO, out);
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		while (in.available() > 0) {
			System.out.println(decoder.decode(in));
		}
	}
	
}
