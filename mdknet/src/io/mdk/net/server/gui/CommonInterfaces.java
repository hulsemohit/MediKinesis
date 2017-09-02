package io.mdk.net.server.gui;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.swing.JTextArea;

public class CommonInterfaces {

	public static class CommonPrinterWriter implements Printers{
		
		PrintWriter writer;
		
		public CommonPrinterWriter(PrintWriter writer){
			this.writer = writer;
		}

		@Override
		public void print(String message) {
			writer.println(message);
		}
		
	}
	
	public static class CommonPrinterStream implements Printers{
		
		PrintStream stream;
		
		public CommonPrinterStream(PrintStream stream){
			this.stream = stream;
		}

		@Override
		public void print(String message) {
			stream.println(message);
		}
		
	}
	
	public static class CommonTextAreaPrinter implements Printers{
		
		JTextArea textArea;
		 
		public CommonTextAreaPrinter(JTextArea textArea) {
			super();
			this.textArea = textArea;
		}



		@Override
		public void print(String message) {
			System.out.println("called!");
			textArea.append(message + System.lineSeparator());
		}
		
	}
	
	public static CommonPrinterWriter getPrinter(PrintWriter writer){
		return new CommonPrinterWriter(writer);
	}
	
	public static CommonPrinterStream getPrinter(PrintStream stream){
		return new CommonPrinterStream(stream);
	}
	
	public static CommonTextAreaPrinter getPrinter(JTextArea area){
		return new CommonTextAreaPrinter(area);
	}
	
}
