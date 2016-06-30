package com.phoenixkahlo.eclipse.world.entity;

import com.phoenixkahlo.eclipse.world.BasicBackground;
import com.phoenixkahlo.eclipse.world.ImageResource;

public class SpaceBackground extends BasicBackground {

	public SpaceBackground() {
		if (ImageResource.STARS_1.image() != null) 
			injectTexture(ImageResource.STARS_1.image(), 30);
	}
	
}
