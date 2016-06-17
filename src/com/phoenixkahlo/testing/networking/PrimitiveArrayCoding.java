package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.phoenixkahlo.networking.ArrayDecoder;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.PrimitiveDecoder;

public class PrimitiveArrayCoding {

	public static void main(String[] args) throws Exception {
		EncodingProtocol encoder = new ArrayEncoder(Object.class);
		DecodingProtocol decoder = new ArrayDecoder(Object.class, new PrimitiveDecoder(Integer.class));
		
		int[] arr = {1, 2, 3, 4, 5};
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(arr, out);
		
		byte[] buffer = out.toByteArray();
		for (int i = 0; i < buffer.length; i++) {
			System.out.println(buffer[i]);
		}
		System.out.println("~~~");
		
		Object obj = decoder.decode(new ByteArrayInputStream(buffer));
		Object[] arr2 = (Object[]) obj;
		for (int i = 0; i < arr2.length; i++) {
			System.out.println(arr2[i]);
		}
	}
	
}
