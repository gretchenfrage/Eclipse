package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class FunctionReceiver {

	private static Random random = new Random();
	
	private Map<Integer, Function> functions = new HashMap<Integer, Function>();
	private InputStream in;
	private DecodingProtocol decoder; // Nullable
	
	public FunctionReceiver(InputStream in, DecodingProtocol decoder) {
		this.in = in;
		this.decoder = decoder;
	}
	
	public FunctionReceiver(InputStream in) {
		this(in, null);
	}
	
	public void registerFunction(int header, Function function) {
		functions.put(header, function);
	}

	/**
	 * @return the randomly generated header
	 */
	public int registerFunction(Function function) {
		int header;
		do {
			header = random.nextInt();
		} while (functions.keySet().contains(functions));
		registerFunction(header, function);
		return header;
	}
	
	public void receive() throws IOException, ProtocolViolationException {
		Function function = functions.get(SerializationUtils.readInt(in));
		if (function == null)
			throw new ProtocolViolationException("invalid header");
		Class<?>[] argTypes = function.getArgTypes();
		Object[] args = new Object[argTypes.length];
		for (int i = 0; i < argTypes.length; i++) {
			args[i] = SerializationUtils.readType(argTypes[i], in, decoder);
		}
		function.invoke(args);
	}
	
}
