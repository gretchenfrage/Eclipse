package com.phoenixkahlo.testing;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.phoenixkahlo.networkingcore.FieldDecoder;
import com.phoenixkahlo.networkingcore.FieldEncoder;
import com.phoenixkahlo.networkingcore.Function;
import com.phoenixkahlo.networkingcore.FunctionBroadcaster;
import com.phoenixkahlo.networkingcore.FunctionReceiver;
import com.phoenixkahlo.networkingcore.FunctionReceiverThread;
import com.phoenixkahlo.networkingcore.InstanceMethod;
import com.phoenixkahlo.networkingcore.ProtocolViolationException;
import com.phoenixkahlo.networkingcore.UnionDecoder;
import com.phoenixkahlo.networkingcore.UnionEncoder;

public class DynamicProtocolTester {

	public static class Printer {
		
		private String str;
		
		public Printer() {}
		
		public Printer(String str) {
			this.str = str;
		}
		
		public void print() {
			System.out.println(str);
		}
		
	}
	
	public static void main(String[] args) throws Exception {
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					UnionEncoder encoder = new UnionEncoder();
					encoder.registerProtocol(0, new FieldEncoder(Printer.class));
					
					ServerSocket ss = new ServerSocket(37524);
					OutputStream out = ss.accept().getOutputStream();
					ss.close();
					
					FunctionBroadcaster broadcaster = new FunctionBroadcaster(out, encoder);
					broadcaster.registerFunction(0, "bind");
					// Where the dynamic binding actually happens
					Printer printer = new Printer("Hello world!");
					int header = broadcaster.registerFunction("print");
					broadcaster.broadcast("bind", printer, header);
					
					broadcaster.broadcast("print");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		new Thread(new Runnable() {
			@SuppressWarnings("resource")
			@Override
			public void run() {
				try {
					UnionDecoder decoder = new UnionDecoder();
					decoder.registerProtocol(0, new FieldDecoder(Printer.class, Printer::new));

					Thread.sleep(200);
					InputStream in = new Socket("localhost", 37524).getInputStream();
					
					FunctionReceiver receiver = new FunctionReceiver(in, decoder);
					receiver.registerFunction(0, new Function() {

						@Override
						public void invoke(Object... args) throws ProtocolViolationException {
							Printer printer = (Printer) args[0];
							int header = (Integer) args[1];
							receiver.registerFunction(header, new InstanceMethod(printer, "print"));
							System.out.println(printer + " bound to " + header);
						}

						@Override
						public Class<?>[] getArgTypes() {
							return new Class<?>[] {Printer.class, int.class};
						}
						
					});
					
					new FunctionReceiverThread(receiver, Exception::printStackTrace).start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
}
