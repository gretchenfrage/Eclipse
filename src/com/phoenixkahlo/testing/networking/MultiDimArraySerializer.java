package com.phoenixkahlo.testing.networking;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networking.ArrayDecoder;
import com.phoenixkahlo.networking.ArrayEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;

public class MultiDimArraySerializer {

	public static void main(String[] args) throws Exception {
		EncodingProtocol encoder = new ArrayEncoder(int[].class, new ArrayEncoder(int.class));
		DecodingProtocol decoder = new ArrayDecoder(int[].class, new ArrayDecoder(int.class));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		encoder.encode(new int[][] {
				{1, 2, 3},
				{4, 5, 6},
				{7, 8, 9}
			}, out);
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		Object obj = decoder.decode(in);
		
		int[][] arr = (int[][]) obj;
		for (int y = 0; y < arr.length; y++) {
			for (int x = 0; x < arr[y].length; x++) {
				System.out.print(arr[y][x] + " ");
			}
			System.out.println();
		}
		
		System.out.println("~~~");
		
		
	}

}
