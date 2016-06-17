package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.UnionEncoder;

public class CircularReferenceTester {

	public static void main(String[] args) throws IllegalArgumentException, IOException {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(0, new FieldEncoder(CircularObj.class, encoder));
		encoder.encode(new CircularObj(), new ByteArrayOutputStream());
	}
	
}
