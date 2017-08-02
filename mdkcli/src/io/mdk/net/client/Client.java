package io.mdk.net.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.mdk.net.utils.Commons.Crypto;
import io.mdk.net.utils.Commons.NLZF;

public class Client {

	private static final Logger LOG = Logger.getLogger(Client.class.getName());
	DataInputStream inputStream;
	DataOutputStream outputStream;
	Socket conn;
	public boolean shutdown = false;
	
	public Client(String host){
		try {
			this.conn = new Socket(host, 6444);
			outputStream = new DataOutputStream(conn.getOutputStream());
			inputStream = new DataInputStream(conn.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cleanup(){
		try {
			inputStream.close();
			outputStream.close();
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected void send(byte[] bytes){
		try {
			outputStream.writeInt(bytes.length);
			outputStream.write(bytes);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	protected byte[] recv(){
		byte[] data = null;
		try {
			int len = inputStream.readInt();
			data = new byte[len];
			inputStream.readFully(data);
		} catch (IOException e) {
			LOG.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return data;
	}
	
	public void write(String msg){
		byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
		byte[] enc = Crypto.encrypt(bytes);
		byte[] lzf = NLZF.b_cmpr(enc);
		send(lzf);
	}
	
	public String read(){
		byte[] lzf = recv();
		byte[] enc = NLZF.b_dcmpr(lzf);
		byte[] ret = Crypto.decrypt(enc);
		return new String(ret);
	}
	
	public boolean checkUser(String username){
		write("chkusr " + username);
		return Boolean.parseBoolean(read());
	}
	
}
