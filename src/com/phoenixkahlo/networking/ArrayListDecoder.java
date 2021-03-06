package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Works for all ArrayList types.
 */
public class ArrayListDecoder implements DecodingProtocol {

	private ArrayDecoder arrayDecoder;
	
	public ArrayListDecoder(DecodingProtocol itemDecoder) {
		arrayDecoder = new ArrayDecoder(Object.class, itemDecoder);
	}
	
	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		if (SerializationUtils.readBoolean(in))
			return null;
		return new ArrayList<Object>(Arrays.asList((Object[]) arrayDecoder.decode(in)));
	}

	@Override
	public EncodingProtocol toEncoder() {
		return new ArrayListEncoder(arrayDecoder.getItemDecoder().toEncoder());
	}
	
}
