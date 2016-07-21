package com.phoenixkahlo.testing.eclipse;

import java.io.File;

import com.phoenixkahlo.cornsnake.Parser;

public class ShipParsingTester {

	public static void main(String[] args) throws Exception {
		File file = new File(ShipParsingTester.class.getClassLoader().getResource(
				"resources/text/basic_ship_1.cos").toURI());
		Object obj = Parser.parse(file);
		System.out.println(obj);
	}
	
}
