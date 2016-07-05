package com.phoenixkahlo.eclipse.server;

import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.InstanceMethod;
import com.phoenixkahlo.utils.MathUtils;

/**
 * Provides methods for networked.
 */
public abstract class NetworkedServerControlHandler implements ServerControlHandler {
	
	private FunctionReceiver receiver;
	private int originalFunctionHeader;
	private int nextFunctionHeader;
	
	public NetworkedServerControlHandler(FunctionReceiver receiver) {
		this.receiver = receiver;

		originalFunctionHeader = MathUtils.RANDOM.nextInt();
		nextFunctionHeader = originalFunctionHeader;
	}
	
	public int getOriginalFunctionHeader() {
		return originalFunctionHeader;
	}
	
	protected void registerReceiveMethod(String name, Class<?>... argTypes) {
		receiver.registerFunction(nextFunctionHeader, new InstanceMethod(this, name, argTypes));
		nextFunctionHeader++;
	}
	
}
