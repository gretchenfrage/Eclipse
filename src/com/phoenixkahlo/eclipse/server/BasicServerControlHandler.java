package com.phoenixkahlo.eclipse.server;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.server.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.DisableableFunction;
import com.phoenixkahlo.networking.InstanceMethod;
import com.phoenixkahlo.utils.MathUtils;

/**
 * Provides methods for networked.
 */
public abstract class BasicServerControlHandler implements ServerControlHandler {
	
	private ClientConnection connection;
	private int originalFunctionHeader;
	private int nextFunctionHeader;
	private List<DisableableFunction> disableables = new ArrayList<DisableableFunction>();
	
	public BasicServerControlHandler(ClientConnection connection) {
		this.connection = connection;

		originalFunctionHeader = MathUtils.RANDOM.nextInt();
		nextFunctionHeader = originalFunctionHeader;
	}
	
	public int getOriginalFunctionHeader() {
		return originalFunctionHeader;
	}
	
	protected void registerReceiveMethod(String name, Class<?>... argTypes) {
		DisableableFunction disableable = new DisableableFunction(new InstanceMethod(this, name, argTypes));
		disableables.add(disableable);
		connection.getReceiver().registerFunction(nextFunctionHeader, disableable);
		nextFunctionHeader++;
	}
	
	protected ClientConnection getConnection() {
		return connection;
	}
	
	protected void queueImpose(int time, Consumer<WorldState> event) {
		connection.getServer().queueEvent(new ImposeEventEvent(time, event));
	}
	
	@Override
	public void disable() {
		for (DisableableFunction disableable : disableables) {
			disableable.disable();
		}
	}
	
}
