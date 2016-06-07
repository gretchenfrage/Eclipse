package com.phoenixkahlo.testing;

public class ThingPrinter {
	
	private String str;
	
	public ThingPrinter() {}
	
	public ThingPrinter(String str) {
		this.str = str;
	}
	
	public void printThing(String append) {
		System.out.println(str + " " + append);
	}
	
}