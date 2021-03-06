package com.phoenixkahlo.networking;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Broadcasts functional calls for FunctionReceiver.
 */
public class FunctionBroadcaster {

	private static Random random = new Random();
	
	private Map<Object, Integer> headers = new HashMap<Object, Integer>();
	private OutputStream out;
	private EncodingProtocol encoder; // Nullable
	
	public FunctionBroadcaster(OutputStream out, EncodingProtocol encoder) {
		this.out = out;
		this.encoder = encoder;
	}
	
	public FunctionBroadcaster(OutputStream out) {
		this(out, null);
	}
	
	public void registerFunction(int header, Object token) {
		headers.put(token, header);
	}
	
	/**
	 * Register a function with the token and the header being the ordinal of the token.
	 */
	public void registerFunctionEnum(Enum<?> token) {
		registerFunction(token.ordinal(), token);
	}
	
	public void registerEnumClass(Class<? extends Enum<?>> clazz) {
		for (Enum<?> token : clazz.getEnumConstants()) {
			registerFunctionEnum(token);
		}
	}
	
	/**
	 * @return the randomly generated header.
	 */
	public int registerFunction(Object token) {
		int header;
		do {
			header = random.nextInt();
		} while (headers.values().contains(headers));
		registerFunction(header, token);
		return header;
	}
	
	/**
	 * Is thread safe.
	 */
	public synchronized void broadcast(Object token, Object ... args) throws IOException,
			IllegalArgumentException {
		if (!headers.containsKey(token))
			throw new IllegalArgumentException("unmapped token: " + token);
		SerializationUtils.writeInt(headers.get(token), out);
		for (Object arg : args) {
			SerializationUtils.writeAny(arg, out, encoder);
		}
	}
	
}
