package io.mdk.net.client;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
//import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import io.mdk.net.client.Client;
import io.mdk.net.utils.Report;

public class Main {

	public static void main(String[] args) {
		try {
			Client client = new Client(InetAddress.getLocalHost().getHostName(), new Client.ExHandler() {
				
				@Override
				public void handle(Exception ex) {
					ex.printStackTrace();
				}
			});
			System.out.println(client.checkUser("nikhil"));
			Report _r = client.view("nikhil.rp", "40");
			System.out.println(client.checkNotes("nikhil.rp"));
			//System.out.println(_r.age);
			showImage(_r.getDetected());
			//showImage(_r.getImgs()[1]);
			//System.out.println(Arrays.toString(_r.truesymptoms));
			//client.report(new Report(new byte[][]{getFromFile(new File("./testcandidate.jpg")), null}, true, null, 23, null), "nikhil");
			//System.out.println(client.gnote("nikhil"));
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
	
	private static BufferedImage frombyte(byte[] data){
		try {
			return ImageIO.read(new ByteArrayInputStream(data));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public static void showImage(byte[] data){
		JFrame jfram = new JFrame();
		JLabel label = new JLabel();
		ImageIcon icon = new ImageIcon(frombyte(data));
		label.setIcon(icon);
		jfram.setSize(icon.getIconWidth(), icon.getIconHeight());
		jfram.setTitle("Display");
		jfram.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jfram.getContentPane().add(label);
		jfram.setVisible(true);
	}

}
