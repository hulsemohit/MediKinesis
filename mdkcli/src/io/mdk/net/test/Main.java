package io.mdk.net.test;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.imageio.ImageIO;

import io.mdk.net.client.Client;
import io.mdk.net.utils.Report;

public class Main {

	public static void main(String[] args) {
		try {
			Client client = new Client(InetAddress.getLocalHost().getHostName());
			System.out.println(client.checkUser("nikhil"));
			//client.report(new Report(new byte[][]{getFromFile(new File("./testcandidate.jpg")), null}, true, null, 23), "nikhil");
			Report rep = client.view("nikhil", "23.4f");
			client.cleanup();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] getFromFile(File f){
		byte[] dat = null;
		try {
			BufferedImage image = ImageIO.read(f);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ImageIO.write(image, "jpg", byteArrayOutputStream);
			dat = byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dat;
	}

}
