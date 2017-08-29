package com.esotericsoftware.minlog;

import java.util.ArrayList;

import io.mdk.net.server.gui.Printers;

public class NeoLog extends Log.Logger {
	
	public static ArrayList<Printers> handlers = new ArrayList<>();
	
	@Override
	protected void print(String message) {
		// Debugger Optimization!
		if(handlers.size() > 0){
			handlers.stream().forEach((out) -> out.print(message));
			super.print(message);
		} else {
			super.print(message);
		}
	}
	
	public void clearHandlers(){
		handlers.clear();
	}
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		handlers.clear();
		super.finalize();
	}
	
}
