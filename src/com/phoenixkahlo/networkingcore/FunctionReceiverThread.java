package com.phoenixkahlo.networkingcore;

import java.io.IOException;
import java.util.function.Consumer;

public class FunctionReceiverThread extends Thread {

	private FunctionReceiver receiver;
	private Consumer<Exception> disconnectionEvent; // Nullable
	
	public FunctionReceiverThread(FunctionReceiver receiver, Consumer<Exception>
			disconnectionEvent) {
		super("FunctionReceiverThread for " + receiver);
		this.receiver = receiver;
		this.disconnectionEvent = disconnectionEvent;
	}
	
	public FunctionReceiverThread(FunctionReceiver receiver) {
		this(receiver, null);
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				receiver.receive();
			}
		} catch (IOException | ProtocolViolationException e) {
			if (disconnectionEvent != null)
				disconnectionEvent.accept(e);
		}
	}
	
}
