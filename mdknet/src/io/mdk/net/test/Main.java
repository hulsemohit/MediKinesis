package io.mdk.net.test;

import io.mdk.net.server.Server;

public class Main {

	public static void main(String[] args) {
		Server.protect(23.4f);
		Server server = new Server(6444, 60000);
		server.start();
	}
	
}
