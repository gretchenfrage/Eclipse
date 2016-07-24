package com.phoenixkahlo.eclipse.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.EclipseCodingProtocol;
import com.phoenixkahlo.eclipse.QueueFunctionFactory;
import com.phoenixkahlo.eclipse.client.ClientFunction;
import com.phoenixkahlo.eclipse.server.event.ClientDisconnectionEvent;
import com.phoenixkahlo.eclipse.server.event.ImposeEventEvent;
import com.phoenixkahlo.eclipse.server.event.VerifyChecksumEvent;
import com.phoenixkahlo.eclipse.world.WorldState;
import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.FunctionBroadcaster;
import com.phoenixkahlo.networking.FunctionReceiver;
import com.phoenixkahlo.networking.FunctionReceiverThread;
import com.phoenixkahlo.networking.InstanceMethod;
import com.phoenixkahlo.utils.CheckSum;
import com.phoenixkahlo.utils.DisconnectionDetectionInputStream;

public class ClientConnection {

	private final String address;
	
	private FunctionBroadcaster broadcaster;
	private FunctionReceiver receiver;
	private FunctionReceiverThread receiverThread;
	private Server server;
	private ServerControlHandler controlHandler; // Nullable
	
	public ClientConnection(Socket socket, Server server) throws IOException {
		this.address = socket.getInetAddress().toString();
		this.server = server;
		
		// Setup network
		broadcaster = new FunctionBroadcaster(socket.getOutputStream(), EclipseCodingProtocol.getEncoder());
		broadcaster.registerEnumClass(ClientFunction.class);

		InputStream in = socket.getInputStream();
		in = new DisconnectionDetectionInputStream(in);
		
		receiver = new FunctionReceiver(in, EclipseCodingProtocol.getDecoder());
		QueueFunctionFactory<Server> factory = new QueueFunctionFactory<Server>(server::queueEvent);
		
		receiver.registerFunction(ServerFunction.IMPOSE_EVENT.ordinal(),
				factory.create(ImposeEventEvent.class, int.class, Consumer.class));
		receiver.registerFunction(ServerFunction.DISCONNECT.ordinal(),
				new InstanceMethod(this, "disconnect"));
		receiver.registerFunction(ServerFunction.REQUEST_SYNCHRONIZE_TIME.ordinal(),
				new InstanceMethod(this, "synchronizeTime"));
		receiver.registerFunction(ServerFunction.VERIFY_CHECKSUM.ordinal(),
				factory.create(VerifyChecksumEvent.class, int.class, CheckSum.class));
		
		assert receiver.areAllOrdinalsRegistered(ServerFunction.class) : "Server function(s) not registered";
		
		receiverThread = new FunctionReceiverThread(receiver, this::disconnect);
	}
	
	public void start() {
		receiverThread.start();
	}
	
	public void broadcastSetContinuum(WorldStateContinuum continuum) throws IOException {
		broadcaster.broadcast(ClientFunction.SET_CONTINUUM, continuum);
	}
	
	public void broadcastBringToTime(int time) throws IOException {
		broadcaster.broadcast(ClientFunction.BRING_TO_TIME, time);
	}
	
	public void broadcastImposeEvent(int time, Consumer<WorldState> event) throws IOException {
		broadcaster.broadcast(ClientFunction.IMPOSE_EVENT, time, event);
	}
	
	public void broadcastRequestVerifyChecksum(int time) throws IOException {
		broadcaster.broadcast(ClientFunction.REQUEST_VERIFY_CHECKSUM, time);
	}
	
	public void broadcastRequestRequestSynchronizeTime() throws IOException {
		broadcaster.broadcast(ClientFunction.REQUEST_REQUEST_SYNCHRONIZE_TIME);
	}
	
	public void setAndBroadcastControlHandler(ServerControlHandler controlHandler) throws IOException {
		if (this.controlHandler != null)
			this.controlHandler.disable();
		this.controlHandler = controlHandler;
		broadcaster.broadcast(ClientFunction.CREATE_CONTROL_HANDLER, controlHandler.getClientHandlerCreator());
	}
	
	public FunctionReceiver getReceiver() {
		return receiver;
	}
	
	public Server getServer() {
		return server;
	}
	
	/**
	 * Is receiver thread safe.
	 */
	public void synchronizeTime() {
		// I think the lack of concurrency precautions is okay... I think...
		try {
			broadcastBringToTime(server.getContinuum().getTime());
		} catch (IOException e) {
			disconnect(e);
		}
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
	}
	
	@Override
	public String toString() {
		return "ClientConnection to " + address;
	}
	
}
