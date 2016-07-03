package com.phoenixkahlo.eclipse.world;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

/**
 * Holds all the image resources loaded from files.
 */
public enum ImageResource {

	STARS_1("stars_1"),
	BANNER_1("banner_1"),
	BANNER_2("banner_2"),
	BALL_1("ball_1"),
	BALL_2("ball_2"),
	BASIC_SHIP_1("basic_ship_1"),
	SQUARE_1("square_1"),
	ARROW_SQUARE("arrow_square"),
	HUMAN_1("human_1");
	
	/**
	 * Should be called by MainMenu.
	 */
	public static void init() throws SlickException {
		for (ImageResource resource : values()) {
			resource.image = ResourceUtils.loadImage(resource.path);
		}
	}
	
	private String path;
	private Image image;
	
	ImageResource(String path) {
		this.path = path;
	}
	
	/**
	 * @return nullable in contexts where rendering is unexpected (the server)
	 */
	public Image image() {
		return image;
	}
	
}
