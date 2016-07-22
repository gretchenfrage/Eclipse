package com.phoenixkahlo.eclipse.world.entity;

import java.io.IOException;
import java.io.InputStream;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.EncodingProtocol;
import com.phoenixkahlo.networking.FieldDecoder;
import com.phoenixkahlo.networking.FieldEncoder;

/**
 * Uses cornsnake to parse a ship from a text file. Made so that each individual type of ship 
 * need not have it's own class. 
 */
public class ParsedShip extends Ship {

	public static EncodingProtocol makeEncoder(EncodingProtocol subEncoder) {
		return new FieldEncoder(ParsedShip.class, subEncoder);
	}
	
	public static DecodingProtocol makeDecoder(DecodingProtocol subDecoder) {
		return new FieldDecoder(ParsedShip.class, ParsedShip::new, subDecoder);
	}
	
	private ParsedShipTemplate schematic;
	
	private ParsedShip() {}
	
	public ParsedShip(ParsedShipTemplate schematic) {
		this.schematic = schematic;
		schematic.apply(this);
	}
	
	@Override
	public void finishDecoding(InputStream in) throws IOException {
		super.finishDecoding(in);
		schematic.apply(this);
	}
	
}