package com.phoenixkahlo.testing.networking;

import java.io.IOException;
import java.io.InputStream;

import com.phoenixkahlo.networking.DecodingProtocol;
import com.phoenixkahlo.networking.ProtocolViolationException;
import com.phoenixkahlo.networking.SerializationUtils;

public class ClassDecoder implements DecodingProtocol {

	@Override
	public Object decode(InputStream in) throws IOException, ProtocolViolationException {
		try {
			return Class.forName(SerializationUtils.readString(in), true, ClassDecoder.class.getClassLoader());
		} catch (ClassNotFoundException e) {
			throw new ProtocolViolationException(e);
		}
	}

}
