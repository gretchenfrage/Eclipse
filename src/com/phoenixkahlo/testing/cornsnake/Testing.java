package com.phoenixkahlo.testing.cornsnake;
import java.io.File;

import com.phoenixkahlo.cornsnake.Parser;

public class Testing {

	public static void main(String[] args) throws Exception {
		System.out.println(Parser.parse(new File("/Users/Phoenix/Desktop/Parse.txt")));
	}

}
