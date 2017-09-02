package io.mdk.net.test;

import java.util.Arrays;

import javax.swing.JOptionPane;

import com.esotericsoftware.minlog.Log;
import com.esotericsoftware.minlog.NeoLog;
import com.esotericsoftware.minlog.Log.Logger;

import io.mdk.net.server.Server;
import io.mdk.net.server.gui.ServerConsoleUI;

public class Main {

//	String[] dirs = {"notes", "report"};
	
	static {
		Log.DEBUG();
		Logger.setPreffered(new NeoLog());
	}
	
	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		if(args.length >= 1){
			if(args[0].equals("gui")){
				//ServerConsoleUI.main(null);
				String pin = JOptionPane.showInputDialog("Enter PIN");
				ServerConsoleUI ui = new ServerConsoleUI();
				ui.setVisible(true);
				Logger.setPreffered(new NeoLog());
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
