package io.mdk.net.test;

import javax.swing.JOptionPane;

import io.mdk.net.server.Server;
import io.mdk.net.server.gui.ServerConsoleUI;

public class Main {

//	String[] dirs = {"notes", "report"};
	
	public static void main(String[] args) {
		if(args.length > 1){
			if(args[0].equals("gui")){
				ServerConsoleUI.main(null);
				String pin = JOptionPane.showInputDialog("Enter PIN");
				Server.protect(Integer.parseInt(pin));
				Server server = new Server(6444, 60000);
				server.start();
			} else {
				String pin = JOptionPane.showInputDialog("Enter PIN");
				Server.protect(Integer.parseInt(pin));
				Server server = new Server(6444, 60000);
				server.start();
			}
		} else {
			String pin = JOptionPane.showInputDialog("Enter PIN");
			Server.protect(Integer.parseInt(pin));
			Server server = new Server(6444, 60000);
			server.start();
		}
	}
	
	//public static createDirs()
	
}
