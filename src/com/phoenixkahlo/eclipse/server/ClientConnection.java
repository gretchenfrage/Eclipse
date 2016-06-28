package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Consumer;

import org.dyn4j.geometry.Vector2;

import com.phoenixkahlo.eclipse.QueueFunctionFactory;
import com.phoenixkahlo.eclipse.EclipseCoderFactory;
import com.phoenixkahlo.eclipse.client.ClientFunction;
import com.phoenixkahlo.eclipse.server.event.ClientDisconnectionEvent;
import com.phoenixkahlo.eclipse.server.event.ClientInitializationEvent;
import com.phoenixkahlo.eclipse.server.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.event.EntityDeletionEvent;
import com.phoenixkahlo.eclipse.world.event.SetWalkingEntityDirectionEvent;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;
import com.phoenixkahlo.networking.InstanceMethod;
import com.phoenixkahlo.utils.DisconnectionDetectionInputStream;

public class ClientConnection {

	private final String address;
	
	private FunctionBroadcaster broadcaster;
	private FunctionReceiverThread receiverThread;
	private Server server;
	private int entityID = -1; // Is -1 to represent lack of entity.
	private boolean isInitialized = false;
	
	public ClientConnection(Socket socket, Server server) throws IOException {
		this.address = socket.getInetAddress().toString();
		this.server = server;
		
		// Setup network
		broadcaster = new FunctionBroadcaster(socket.getOutputStream(), EclipseCoderFactory.makeEncoder());
		broadcaster.registerEnumClass(ClientFunction.class);

		InputStream in = socket.getInputStream();
		in = new DisconnectionDetectionInputStream(in);
		
		FunctionReceiver receiver = new FunctionReceiver(in, EclipseCoderFactory.makeDecoder());
		QueueFunctionFactory<Server> factory = new QueueFunctionFactory<Server>(server::queueEvent);
		
		receiver.registerFunction(ServerFunction.INIT_CLIENT.ordinal(),
				factory.create(ClientInitializationEvent.class, new Object[] {this}, ClientConnection.class));
		receiver.registerFunction(ServerFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		receiver.registerFunction(ServerFunction.SET_DIRECTION.ordinal(),
				new InstanceMethod(this, "setDirection", Vector2.class));
		receiver.registerFunction(ServerFunction.DISCONNECT.ordinal(),
				new InstanceMethod(this, "disconnect"));
		
		assert receiver.areAllOrdinalsRegistered(ServerFunction.class) : "Server function(s) not registered";
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnect);
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
	
	public void broadcastImposeEvent(int time, Consumer<WorldState> event) throws IOException {
		broadcaster.broadcast(ClientFunction.IMPOSE_EVENT, time, event);
	}
	
	public int getEntityID() {
		return entityID;
	}
	
	public void setEntityID(int entityID) {
		this.entityID = entityID;
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	public void setIsInitialized() {
		isInitialized = true;
	}
	
	/**
	 * Is receiver thread safe.
	 * @param cause nullable
	 */
	public void disconnect(Exception cause) {
		if (cause == null)
			disconnect("Unknown cause");
		else
			disconnect(cause.toString());
	}
	
	/**
	 * Is receiver thread safe.
	 */
	public void disconnect(String cause) {
		server.queueEvent(new ClientDisconnectionEvent(this, cause));
	}
	
	/**
	 * Is receiver thread safe.
	 */
	public void disconnect() {
		disconnect("Voluntary disconnection");
	}
	
	public void onDisconnection(String cause) {
		receiverThread.terminate();
		if (entityID != -1)
			server.imposeEvent(new EntityDeletionEvent(entityID));
	}
	
	@Override
	public String toString() {
		return "ClientConnection to " + address;
	}
	
	public void setDirection(Vector2 direction) {
		if (entityID != -1)
			server.queueEvent(new ImposeEventEvent(server.getContinuum().getTime(),
					new SetWalkingEntityDirectionEvent(entityID, direction)));
	}
	
}
