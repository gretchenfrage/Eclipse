package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.phoenixkahlo.eclipse.ConstructQueueFunctionFactory;
import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.client.ClientFunction;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;

public class ClientConnection {

	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	
	private int entityID = -1; // Is -1 to represent lack of entity.

	public ClientConnection(Socket socket, Server server) {
		// Setup network
		InputStream in = null;
		OutputStream out = null;
		try {
			in = socket.getInputStream();
			out = socket.getOutputStream();
		} catch (IOException e) {
			disconnection(e);
			return;
		}
		
		broadcaster = new FunctionBroadcaster(out, EclipseCoderFactory.makeEncoder());
		broadcaster.registerEnumClass(ClientFunction.class);
		
		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		ConstructQueueFunctionFactory<Server> factory = new ConstructQueueFunctionFactory<Server>(server::queueEvent);
		
		receiver.registerFunction(ServerFunction.INIT_CLIENT.ordinal(),
				factory.create(InitClientEvent.class, new Object[] {this}, ClientConnection.class));
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnection);
		System.out.println("Connection with " + socket + " created");
	}
	
	public void start() {
		receiverThread.start();
		System.out.println("Connection " + this + " started");
	}
	
	private void broadcast(ClientFunction function, Object... args) {
		System.out.println(this + " trying to broadcast " + function);
		try {
			broadcaster.broadcast(function, args);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			disconnection(e);
		}
	}
	
	public void broadcastSetTime(int time) {
		broadcast(ClientFunction.SET_TIME, time);
	}
	
	public void broadcastSetWorldState(WorldState state) {
		broadcast(ClientFunction.SET_WORLD_STATE, state);
	}
	
	public void broadcastSetPerspectiveToEntity(int id) {
		broadcast(ClientFunction.SET_PERSPECTIVE_TO_ENTITY, id);
	}
	
	public int getEntityID() {
		return entityID;
	}
	
	public void setEntityID(int entityID) {
		this.entityID = entityID;
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
