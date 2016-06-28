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
	BALL_1("ball_1");
	
	/**
	 * Should be called by MainMenu.
	 */
	public static void init() throws SlickException {
		for (ImageResource resource : ImageResource.values()) {
			resource.image = ResourceUtils.loadImage(resource.path);
		}
	}
	
	private String path;
	private Image image;
	
	ImageResource(String path) {
		this.path = path;
	}
	
	public Image image() {
		return image;
	}
	
}
