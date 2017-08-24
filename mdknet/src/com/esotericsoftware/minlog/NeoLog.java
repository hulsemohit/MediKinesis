package com.esotericsoftware.minlog;

import java.io.PrintWriter;
import java.util.ArrayList;

public class NeoLog extends Log.Logger {
	
	public ArrayList<PrintWriter> handlers = new ArrayList<>();
	
	@Override
	protected void print(String message) {
		handlers.stream().forEach((out) -> out.println(message));
		super.print(message);
	}
	
}
