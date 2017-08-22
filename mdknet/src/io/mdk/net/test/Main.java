package io.mdk.net.test;

import io.mdk.net.server.Server;

public class Main {

//	String[] dirs = {"notes", "report"};
	
	public static void main(String[] args) {
		Server.protect(Float.parseFloat(args[0]));
		Server server = new Server(6444, 60000);
		server.start();
	}
	
	//public static createDirs()
	
}
