package com.phoenixkahlo.eclipse.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.eclipse.world.WorldStateContinuum;
import com.phoenixkahlo.networking.ClientWaiter;

public class Server {

	/**
	 * @param args {port}
	 */
	public static void main(String[] args) {
		args = new String[] {"58892"};
		
		new Server(Integer.parseInt(args[0])).start();
	}
	
	private ClientWaiter waiter;
	private List<Consumer<Server>> eventQueue = new ArrayList<Consumer<Server>>();
	private List<ClientConnection> clients = new ArrayList<ClientConnection>();
	private WorldStateContinuum continuum;
	
	public Server(int port) {
		waiter = new ClientWaiter(
				(Socket socket) -> queueEvent(new ClientConnectionEvent(socket)),
				port,
				Exception::printStackTrace);
		continuum = new WorldStateContinuum();
	}
	
	public void tick() {
		continuum.tick();
		while (!eventQueue.isEmpty()) {
			eventQueue.remove(0).accept(this);
		}
	}
	
	public void start() {
		waiter.start();
		new TickerThread(this::tick, 1_000_000_000 / 60).start();
		System.out.println("Server started");
	}
	
	public void queueEvent(Consumer<Server> event) {
		eventQueue.add(event);
	}
	
	public void addClient(ClientConnection client) {
		clients.add(client);
		client.start();
	}
	
	public WorldStateContinuum getContinuum() {
		return continuum;
	}
	
}
