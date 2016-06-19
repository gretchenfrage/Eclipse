package com.phoenixkahlo.eclipse.world;

import java.io.InputStream;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class ResourceUtils {

	private ResourceUtils() {}
	
	private static ClassLoader loader = ResourceUtils.class.getClassLoader();
	
	public static InputStream getResourceStream(String name) {
		return loader.getResourceAsStream(name);
	}
	
	/**
	 * @return image from that resource path
	 */
	public static Image loadImageAbs(String path) throws SlickException {
		return new Image(getResourceStream(path), path, false);
	}
	
	/**
	 * @return image from the resource path resources/images/name.png
	 */
	public static Image loadImage(String name) throws SlickException {
		return loadImageAbs("resources/images/" + name + ".png");
	}
	
	/**
	 * @return sound from that resource path.
	 */
	public static Sound loadAbsSound(String path) throws SlickException {
		return new Sound(getResourceStream(path), path);
	}
	
	/**
	 * @return sound from that resource path in resources/audio/name.wav
	 */
	public static Sound loadSound(String name) throws SlickException {
		return loadAbsSound("resources/audio/" + name + ".wav");
	}
	
}
