package com.phoenixkahlo.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldEncoder;

/**
 * A compounded, 256B checksum of an InputStream
 */
public class CheckSum {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(CheckSum.class, CheckSum::new, subEncoder);
	}
	
	private int[] compound;
	
	private CheckSum() {}
	
	public CheckSum(InputStream in) throws IOException {
		byte[] sums = new byte[256];
		while (in.available() > 0)
			sums[in.read()]++;
		compound = new int[64];
		for (int i = 0; i < 256; i++) {
			compound[i / 4] |= sums[i] << ((i % 4) * 8);
		}
	}
	
	public boolean equals(CheckSum checksum) {
		for (int i = 0; i < 64; i++) {
			if (compound[i] != checksum.compound[i])
				return false;
		}
		return true;
	}
	
	public void saveVisualization(File file) throws IOException {
		BufferedImage image = new BufferedImage(80, 80, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				for (int xd = 0; xd < 10; xd++) {
					for (int yd = 0; yd < 10; yd++) {
						image.setRGB(x * 10 + xd, y * 10 + yd, compound[x * 8 + y]);
					}
				}
			}
		}
		ImageIO.write(image, "png", file);
	}
	
}
