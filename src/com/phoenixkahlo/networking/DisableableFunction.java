package com.phoenixkahlo.networking;

/**
 * Wraps a function and allows it to be disabled from another thread, 
 * after which all calls to the function will be ignored.
 */
public class DisableableFunction implements Function {

	private Function subFunction;
	private boolean enabled = true;
	
	public DisableableFunction(Function subFunction) {
		this.subFunction = subFunction;
	}

	public synchronized void disable() {
		enabled = false;
	}
	
	@Override
	public synchronized void invoke(Object... args) throws ProtocolViolationException {
		if (enabled)
			subFunction.invoke(args);
	}

	@Override
	public Class<?>[] getArgTypes() {
		return subFunction.getArgTypes();
	}
	
}
