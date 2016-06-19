package com.phoenixkahlo.eclipse;

import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.ArrayListDecoder;
import com.phoenixkahlo.networking.ArrayListEncoder;
import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;
import com.phoenixkahlo.networking.UnionDecoder;
import com.phoenixkahlo.networking.UnionEncoder;

public class EclipseCoderFactory {

	private EclipseCoderFactory() {}
	
	public static EncodingProtocol makeEncoder() {
		UnionEncoder encoder = new UnionEncoder();
		encoder.registerProtocol(CodableType.ARRAYLIST.ordinal(),
				new ArrayListEncoder(encoder));
		encoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldEncoder(WorldState.class, encoder));
		return encoder;
	}
	
	public static DecodingProtocol makeDecoder() {
		UnionDecoder decoder = new UnionDecoder();
		decoder.registerProtocol(CodableType.ARRAYLIST.ordinal(),
				new ArrayListDecoder(decoder));
		decoder.registerProtocol(CodableType.WORLD_STATE.ordinal(),
				new FieldDecoder(WorldState.class, WorldState::new, decoder));
		return decoder;
	}
	
}
