package com.phoenixkahlo.eclipse.world.entity;

import com.phoenixkahlo.eclipse.ImageResource;
import com.phoenixkahlo.eclipse.world.BasicBackground;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

public class SpaceBackground extends BasicBackground {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(SpaceBackground.class, SpaceBackground::new, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(SpaceBackground.class, SpaceBackground::new, subDecoder);
	}
	
	public SpaceBackground() {
		if (ImageResource.STARS_1.image() != null) 
			injectTexture(ImageResource.STARS_1.image(), 30);
	}
	
}
