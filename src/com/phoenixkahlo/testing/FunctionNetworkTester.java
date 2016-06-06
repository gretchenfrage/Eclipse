package com.phoenixkahlo.testing;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.phoenixkahlo.networkingcore.FieldDecoder;
import com.phoenixkahlo.networkingcore.FieldEncoder;
import com.phoenixkahlo.networkingcore.FunctionBroadcaster;
import com.phoenixkahlo.networkingcore.FunctionReceiver;
import com.phoenixkahlo.networkingcore.FunctionReceiverThread;
import com.phoenixkahlo.networkingcore.InstanceMethod;
import com.phoenixkahlo.networkingcore.UnionDecoder;
import com.phoenixkahlo.networkingcore.UnionEncoder;

public class FunctionNetworkTester {
	
	public static void main(String[] args) throws Exception {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(0, new FieldEncoder(Foo.class, encoder));
		encoder.registerProtocol(1, new FieldEncoder(Bar.class, encoder));
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(0, new FieldDecoder(Foo.class, Foo::new, decoder));
		decoder.registerProtocol(1, new FieldDecoder(Bar.class, Bar::new, decoder));
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		final Object testMethodToken = new Object();
		FunctionBroadcaster broadcaster = new FunctionBroadcaster(out, encoder);
		broadcaster.registerFunction(0, testMethodToken);
		
		broadcaster.broadcast(testMethodToken, "hello world!", 5);
		broadcaster.broadcast(testMethodToken, "lenny", 1);
		broadcaster.broadcast(testMethodToken, "goodbye!", 3);
		out.write(50); // intentionally trigger a ProtocolViolationException
		
		byte[] buffer = out.toByteArray();
		InputStream in = new ByteArrayInputStream(buffer);
		FunctionReceiver receiver = new FunctionReceiver(in, decoder);
		receiver.registerFunction(0,
				new InstanceMethod(FunctionNetworkTester.class, "testMethod", String.class, int.class));
		FunctionReceiverThread thread = new FunctionReceiverThread(receiver, Exception::printStackTrace);
		thread.start();
	}
	
	public static void testMethod(String str, int times) {
		for (int i = 0; i < times; i++) {
			System.out.println(str);
		}
	}
	
}
