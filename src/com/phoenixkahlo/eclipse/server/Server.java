package com.phoenixkahlo.eclipse.server;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.phoenixkahlo.networking.ClientWaiter;

public class Server {

	/**
	 * @param args {"port"}
	 */
	public static void main(String[] args) {
		args = new String[] {"58892"};
		
		new Server(Integer.parseInt(args[0])).start();
	}
	
	private ClientWaiter waiter;
	private List<Consumer<Server>> eventQueue = new ArrayList<Consumer<Server>>();
	private List<ClientConnection> clients = new ArrayList<ClientConnection>();
	
	public Server(int port) {
		waiter = new ClientWaiter(
				(Socket socket) -> queueEvent(new ClientConnectionEvent(socket)),
				port,
				Exception::printStackTrace);
	}
	
	public void start() {
		waiter.start();
	}
	
	public void queueEvent(Consumer<Server> event) {
		eventQueue.add(event);
	}
	
	public void addClient(ClientConnection client) {
		clients.add(client);
		client.start();
	}
	
}
