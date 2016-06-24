package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.ConstructQueueFunctionFactory;
import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.client.ClientFunction;
import com.phoenixkahlo.eclipse.server.event.ClientDisconnectionEvent;
import com.phoenixkahlo.eclipse.server.event.ClientInitializationEvent;
import com.phoenixkahlo.eclipse.server.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;

public class ClientConnection {

	private FunctionBroadcaster broadcaster;
	private Thread receiverThread;
	
	private int entityID = -1; // Is -1 to represent lack of entity.

	public ClientConnection(Socket socket, Server server) throws IOException {
		// Setup network
		broadcaster = new FunctionBroadcaster(socket.getOutputStream(), EclipseCoderFactory.makeEncoder());
		broadcaster.registerEnumClass(ClientFunction.class);
		
		FunctionReceiver receiver = new FunctionReceiver(socket.getInputStream(), EclipseCoderFactory.makeDecoder());
		ConstructQueueFunctionFactory<Server> factory = new ConstructQueueFunctionFactory<Server>(server::queueEvent);
		
		receiver.registerFunction(ServerFunction.INIT_CLIENT.ordinal(),
				factory.create(ClientInitializationEvent.class, new Object[] {this}, ClientConnection.class));
		receiver.registerFunction(ServerFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		
		receiverThread = new FunctionReceiverThread(receiver,
				(Exception e) -> server.queueEvent(new ClientDisconnectionEvent(this, e.toString())));
	}
	
	public void start() {
		receiverThread.start();
	}
	
	public void broadcastSetTime(int time) throws IOException {
		broadcaster.broadcast(ClientFunction.SET_TIME, time);
	}
	
	public void broadcastSetWorldState(WorldState state) throws IOException {
		broadcaster.broadcast(ClientFunction.SET_WORLD_STATE, state);
	}
	
	public void broadcastSetPerspectiveToEntity(int id) throws IOException {
		broadcaster.broadcast(ClientFunction.SET_PERSPECTIVE_TO_ENTITY, id);
	}
	
	public void broadcastImposeEvent(int time, Consumer<WorldState> event) throws IOException {
		broadcaster.broadcast(ClientFunction.IMPOSE_EVENT, time, event);
	}
	
	public int getEntityID() {
		return entityID;
	}
	
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}
	
	public void disconnected(String cause) {
	}
	
}
