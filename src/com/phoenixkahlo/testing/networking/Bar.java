package com.phoenixkahlo.testing.networking;

public class Bar {
	
	private Foo foo = new Foo();
	private byte[] bin = {3, 6, 7, 3, 5};
	private Object nullable = null;
	
	public String toString() {
		StringBuilder out = new StringBuilder("Bar:{foo=" + foo + ",bin=[");
		for (byte b : bin)
			out.append(b + ",");
		return out.toString() + "],nullable=" + nullable + "}";
	}
	
}