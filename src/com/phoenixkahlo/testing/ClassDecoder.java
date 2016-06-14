package com.phoenixkahlo.testing;

import java.io.IOException;
import java.io.InputStream;

import com.phoenixkahlo.networkingcore.DecodingProtocol;
import com.phoenixkahlo.networkingcore.ProtocolViolationException;
import com.phoenixkahlo.networkingcore.SerializationUtils;

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
