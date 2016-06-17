package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class PrimitiveFields {

	public static void main(String[] args) throws Exception {
		// Conclusion: type coders do indeed work for autoboxed primitives
		
		EncodingProtocol encoder = new FieldEncoder(Integer.class);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(42, out);
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		DecodingProtocol decoder = new FieldDecoder(Integer.class, () -> new Integer(0));
		Object obj = decoder.decode(in);
		System.out.println(obj);
	}

}
