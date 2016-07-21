package com.phoenixkahlo.eclipse.world.entity;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public enum FileResource {
	
	BASIC_SHIP_1("basic_ship_1.cos");
	;
	
	private File file;
	
	FileResource(String path) {
		try {
			URI uri = FileResource.class.getClassLoader().getResource("resources/text/" + path).toURI();
			file = new File(uri);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}
	
	public File getFile() {
		return file;
	}
	
}
