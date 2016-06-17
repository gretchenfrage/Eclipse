package com.phoenixkahlo.networking;

import java.lang.reflect.Method;

/**
 * Subclasses are expected to provide a method 
 * public void invokeAdapted([any combination of args]);
 */
public class FunctionAdapter implements Function {
	
	private Function function;
	
	public FunctionAdapter() {
		Method[] methods = getClass().getMethods();
		Method method = null;
		for (Method item : methods) {
			if (item.getName().equals("invokeAdapted")) {
				if (method == null)
					method = item;
				else
					throw new IllegalStateException("multiple methods named invokeAdapted");
			}
		}
		if (method == null) throw new IllegalStateException("no methods named invokeAdapted");
		function = new InstanceMethod(this, method);
	}

	@Override
	public void invoke(Object... args) throws ProtocolViolationException {
		function.invoke(args);
	}

	@Override
	public Class<?>[] getArgTypes() {
		return function.getArgTypes();
	}
	
}
