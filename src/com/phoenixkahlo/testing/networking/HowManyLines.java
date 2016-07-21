package com.phoenixkahlo.testing.networking;

import java.io.File;
import java.util.Arrays;
import java.util.Scanner;

public class HowManyLines {

	public static void main(String[] args) throws Exception {
		//System.out.println(lines(new File("C:\\Users\\Phoenix\\Desktop\\Java\\Eclipse\\src")));
		System.out.println(lines(new File("/Users/Phoenix/Desktop/Java/Eclipse2/src")));
	}
	
	public static int lines(File file) {
		if (file.isDirectory()) {
			if (file.getName().equals("testing"))
				return 0;
			else
				return Arrays.stream(file.listFiles()).mapToInt(HowManyLines::lines).sum();
		} else if (file.getName().length() >= 5 &&
				file.getName().substring(file.getName().length() - 5).equals(".java")) {
			try (Scanner scanner = new Scanner(file)) {
				int lines = 0;
				while (scanner.hasNextLine()) {
					scanner.nextLine();
					lines++;
				}
				return lines;
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		} else {
			return 0;
		}
	}

}
