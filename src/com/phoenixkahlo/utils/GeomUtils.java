package com.phoenixkahlo.utils;

import org.dyn4j.geometry.Rectangle;

public class GeomUtils {

	private GeomUtils() {}
	
	public static Rectangle makeRect(double x1, double y1, double x2, double y2) {
		Rectangle out = new Rectangle(x2 - x1, y2 - y1);
		out.translate((x2 - x1) / 2, (y2 - y1) / 2);
		out.translate(x1, y1);
		return out;
	}
	
}
