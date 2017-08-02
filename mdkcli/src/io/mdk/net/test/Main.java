package io.mdk.net.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.mdk.net.client.Client;

public class Main {

	public static void main(String[] args) {
		try {
			Client client = new Client(InetAddress.getLocalHost().getHostName());
			System.out.println(client.checkUser("nikhil"));
			client.cleanup();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

}
