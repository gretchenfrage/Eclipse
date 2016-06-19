package com.phoenixkahlo.testing.networking;

import java.net.Socket;

import com.phoenixkahlo.networking.ClientWaiter;

public class ClientWaiterInterruptionTester {

	public static void main(String[] args) {
		ClientWaiter waiter = new ClientWaiter(
				(Socket socket) -> System.out.println(socket),
				37895,
				Exception::printStackTrace);
		waiter.start();
		
		System.out.println("sleeping");
		try {
			Thread.sleep(2_000);
		} catch (Exception e) {}
		
		System.out.println("interrupting");
		waiter.terminate();
	}
	
}
