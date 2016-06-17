package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networking.ArrayDecoder;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;

public class ArraySerializer {

	public static void main(String[] args) throws Exception {
		EncodingProtocol encoder = new ArrayEncoder(int.class);
		DecodingProtocol decoder = new ArrayDecoder(int.class);
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(new int[] {1,  2, 3, 4, 5, 4, 3, 2, 1}, out);
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		Object obj = decoder.decode(in);
		
		int[] arr = (int[]) obj;
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
		}
		
	}

}
