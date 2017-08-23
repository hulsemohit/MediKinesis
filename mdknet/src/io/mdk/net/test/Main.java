package io.mdk.net.test;

import javax.swing.JOptionPane;

import io.mdk.net.server.Server;

public class Main {

//	String[] dirs = {"notes", "report"};
	
	public static void main(String[] args) {
		String pin = JOptionPane.showInputDialog("Enter PIN");
		Server.protect(Float.parseFloat(pin));
		Server server = new Server(6444, 60000);
		server.start();
	}
	
	//public static createDirs()
	
}
