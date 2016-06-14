package com.phoenixkahlo.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networkingcore.ArrayDecoder;
import com.phoenixkahlo.networkingcore.ArrayEncoder;
import com.phoenixkahlo.networkingcore.DecodingProtocol;
import com.phoenixkahlo.networkingcore.EncodingProtocol;

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
