package com.phoenixkahlo.testing.networking;

import java.io.File;
import java.util.Scanner;

public class HowManyLines {

	public static void main(String[] args) throws Exception {
		System.out.println(lines(new File("C:\\Users\\Phoenix\\Desktop\\Java\\Eclipse\\src")));
	}
	
	public static int lines(File file) throws Exception {
		if (file.isDirectory()) {
			if (file.getName().equals("testing"))
				return 0;
			int sum = 0;
			for (File sub : file.listFiles()) {
				sum += lines(sub);
			}
			return sum;
		} else if (file.getName().length() >= 5 &&
				file.getName().substring(file.getName().length() - 5).equals(".java")) {
			Scanner scanner = new Scanner(file);
			int lines = 0;
			while (scanner.hasNextLine()) {
				scanner.nextLine();
				lines++;
			}
			scanner.close();
			return lines;
		} else {
			return 0;
		}
	}

}
