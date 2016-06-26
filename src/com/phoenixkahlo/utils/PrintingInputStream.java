package com.phoenixkahlo.utils;

import java.io.IOException;
import java.io.InputStream;

public class PrintingInputStream extends InputStream {

	private InputStream in;
	
	public PrintingInputStream(InputStream in) {
		this.in = in;
	}

	@Override
	public int read() throws IOException {
		int read = in.read();
		System.out.println("read: " + read);
		return read;
	}
	
}
