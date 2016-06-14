package com.phoenixkahlo.testing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.phoenixkahlo.networkingcore.FieldEncoder;
import com.phoenixkahlo.networkingcore.UnionEncoder;

public class CircularReferenceTester {

	public static void main(String[] args) throws IllegalArgumentException, IOException {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(0, new FieldEncoder(CircularObj.class, encoder));
		encoder.encode(new CircularObj(), new ByteArrayOutputStream());
	}
	
}
