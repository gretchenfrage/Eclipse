package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Works for all ArrayList types.
 */
public class ArrayListDecoder implements DecodingProtocol {

	private DecodingProtocol arrayDecoder;
	
	public ArrayListDecoder(DecodingProtocol itemDecoder) {
		arrayDecoder = new ArrayDecoder(Object.class, itemDecoder);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {		
		return new ArrayList<Object>(Arrays.asList((Object[]) arrayDecoder.decode(in)));
	}

}
