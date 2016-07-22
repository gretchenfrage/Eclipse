package com.phoenixkahlo.testing.eclipse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.world.entity.ParsedShip;
import com.phoenixkahlo.eclipse.world.entity.ParsedShipTemplate;
import com.phoenixkahlo.utils.PrintingInputStream;
import com.phoenixkahlo.utils.PrintingOutputStream;

public class ParsedShipIntegrityTester {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		
		ParsedShip ship = new ParsedShip(ParsedShipTemplate.BASIC_SHIP_1);
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream out = new PrintingOutputStream(bout);
		EclipseCodingProtocol.getEncoder().encode(ship, out);
		
		System.out.println("----------");
		
		InputStream in = new ByteArrayInputStream(bout.toByteArray());
		in = new PrintingInputStream(in);
		EclipseCodingProtocol.getDecoder().decode(in);
		System.out.println("testing remaining");
		for (int i = 0; i < 10; i++) {
			in.read();
		}
		
	}
	
}
