package io.mdk.net.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.mdk.net.utils.Report;
import io.mdk.net.utils.Commons.Crypto;
import io.mdk.net.utils.Commons.GsonForm;
import io.mdk.net.utils.Commons.NLZF;

public class Client {
	
	public interface ExHandler {
		void handle(Exception ex);
	}

	private static final Logger LOG = Logger.getLogger(Client.class.getName());
	DataInputStream inputStream;
	DataOutputStream outputStream;
	Socket conn;
	public boolean shutdown = false;
	public ExHandler exHandler;
	
	public Client(String host, ExHandler run){
		this.exHandler = run;
		try {
			this.conn = new Socket(host, 6444);
			outputStream = new DataOutputStream(conn.getOutputStream());
			inputStream = new DataInputStream(conn.getInputStream());
		} catch (IOException e) {
			run.handle(e);
		}
	}
	
	public void cleanup(){
		try {
			inputStream.close();
			outputStream.close();
			conn.close();
		} catch (IOException e) {
			exHandler.handle(e);
			e.printStackTrace();
		}
	}
	
	protected void send(byte[] bytes){
		try {
			outputStream.writeInt(bytes.length);
			outputStream.write(bytes);
		} catch (IOException e) {
			exHandler.handle(e);
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
			exHandler.handle(e);
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
	
	public boolean report(Report _r, String username){
		write("repr " + username);
		write(GsonForm.to(_r));
		return Boolean.parseBoolean(read());
	}
	
	public Report view(String user, String pin){
		write("view " + pin + " " + user);
		String ins = read();
		if(ins.equals("false")){
			return null;
		} else {
			Report _rp = GsonForm.from(ins, Report.class);
			return _rp;
		}
	}
	
	/**
	 * Set a note on the server
	 * @param user The User name of the user whose note you want to set
	 * @param pin The Server's constant pin
	 * @param note The actual note.
	 * @return boolean, whether the note was successful or not
	 */
	public boolean note(String user, String pin, String note){
		write("note " + pin + " " + user);
		write(note);
		return Boolean.parseBoolean(read());
	}
	
	public String gnote(String user){
		write("gnote " + user);
		return read();
	}
	
	public String[] listReports(String pin){
		write("avrpr " + pin);
		String in = read();
		return GsonForm.from(in, String[].class);
	}
	
	public boolean checkNotes(String user){
		write("chknts " + user);
		String in = read();
		return Boolean.parseBoolean(in);
	}
	
}
