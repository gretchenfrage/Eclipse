package com.phoenixkahlo.testing.eclipse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.world.entity.Player;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.utils.PrintingOutputStream;

public class PlayerCodingTester {

	public static void main(String[] args) throws IOException, ProtocolViolationException {
		Player player = new Player();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		OutputStream out = new PrintingOutputStream(bout);
		EclipseCodingProtocol.ENCODER.encode(player, out);
		InputStream in = new ByteArrayInputStream(bout.toByteArray());
		EclipseCodingProtocol.DECODER.decode(in);
	}

}
