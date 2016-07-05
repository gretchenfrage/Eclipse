package com.phoenixkahlo.eclipse.server;

import java.util.ArrayList;
import java.util.List;

import com.phoenixkahlo.networking.DisableableFunction;
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
	private List<DisableableFunction> disableables = new ArrayList<DisableableFunction>();
	
	public NetworkedServerControlHandler(FunctionReceiver receiver) {
		this.receiver = receiver;

		originalFunctionHeader = MathUtils.RANDOM.nextInt();
		nextFunctionHeader = originalFunctionHeader;
	}
	
	public int getOriginalFunctionHeader() {
		return originalFunctionHeader;
	}
	
	protected void registerReceiveMethod(String name, Class<?>... argTypes) {
		DisableableFunction disableable = new DisableableFunction(new InstanceMethod(this, name, argTypes));
		disableables.add(disableable);
		receiver.registerFunction(nextFunctionHeader, disableable);
		nextFunctionHeader++;
	}
	
	@Override
	public void disable() {
		for (DisableableFunction disableable : disableables) {
			disableable.disable();
		}
	}
	
}
