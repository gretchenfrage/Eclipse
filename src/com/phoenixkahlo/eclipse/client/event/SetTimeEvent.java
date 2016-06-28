package com.phoenixkahlo.eclipse.client.event;

import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.client.ClientConnectionState;

public class SetTimeEvent implements Consumer<ClientConnectionState> {

	private int time;
	
	public SetTimeEvent(int time) {
		this.time = time;
	}
	
	@Override
	public void accept(ClientConnectionState client) {
		client.setTime(time);
	}

}
