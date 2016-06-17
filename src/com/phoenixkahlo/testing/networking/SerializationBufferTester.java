package com.phoenixkahlo.testing.networking;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerializationBufferTester {

	public static void main(String[] args) throws IOException {
		SerializationBufferBroken buffer = new SerializationBufferBroken(4);
		
		OutputStream out;
		
		out = buffer.startPartition("one");
		out.write(42);
		out.write(7);
		out.write(3);
		out.close();
		
		out = buffer.startPartition("two");
		out.write(52);
		out.write(32);
		out.write(89);
		out.close();
		
		out = buffer.startPartition("three");
		out.write(64);
		out.write(36);
		out.write(46);
		out.close();
		
		InputStream in;
		
		System.out.println("one");
		in = buffer.readPartition("one");
		while (in.available() > 0) {
			System.out.println(in.read());
		}
		in.close();

		System.out.println("two");
		in = buffer.readPartition("two");
		while (in.available() > 0) {
			System.out.println(in.read());
		}
		in.close();
		
		System.out.println("three");
		in = buffer.readPartition("three");
		while (in.available() > 0) {
			System.out.println(in.read());
		}
		in.close();
	}
	
}
