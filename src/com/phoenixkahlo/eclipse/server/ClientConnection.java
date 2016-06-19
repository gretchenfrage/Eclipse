package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.phoenixkahlo.eclipse.ClientFunction;
import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.ServerFunction;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;

public class ClientConnection {

	private Server server;
	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	
	public ClientConnection(Socket socket, Server server) {
		this.server = server;
		
		// Setup network
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			disconnection(e);
		}
		
		// Setup broadcaster
		broadcaster = new FunctionBroadcaster(out, EclipseCoderFactory.makeEncoder());
		broadcaster.registerFunctionEnum(ClientFunction.SET_TIME);
		broadcaster.registerFunctionEnum(ClientFunction.SET_WORLD_STATE);
		broadcaster.registerFunctionEnum(ClientFunction.SET_PERSPECTIVE_TO_ENTITY);
		
		// Setup receiver
		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		receiver.registerFunction(ServerFunction.INIT_CLIENT.ordinal(),
				);
	}
	
	public void start() {
		
	}
	
	public void queueInitClient() {
		
	}
	
	/**
	 * @param cause nullable.
	 */
	private void disconnection(Exception cause) {
		if (cause != null) {
			System.out.println("Disconnecting " + this + " because:");
			cause.printStackTrace(System.out);
		}
	}
	
}
