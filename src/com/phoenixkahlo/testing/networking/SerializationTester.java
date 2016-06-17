package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;

public class SerializationTester {

	public static void main(String[] args) throws Exception {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(0, new FieldEncoder(Foo.class, encoder));
		encoder.registerProtocol(1, new FieldEncoder(Bar.class, encoder));
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(0, new FieldDecoder(Foo.class, Foo::new, decoder));
		decoder.registerProtocol(1, new FieldDecoder(Bar.class, Bar::new, decoder));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(new Bar(), out);
		byte[] buffer = out.toByteArray();
		InputStream in = new ByteArrayInputStream(buffer);
		System.out.println(decoder.decode(in));
	}

}
