package io.mdk.net.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.mdk.net.json_objects.Report;
import io.mdk.net.utils.Commons.Crypto;
import io.mdk.net.utils.Commons.GsonForm;
import io.mdk.net.utils.Commons.NLZF;

public class Server extends Thread {

	public int port;
	public int timeout;
	ServerSocket server;
	public boolean shutdown = false;
	public static final Logger LOG = Logger.getLogger(Server.class.getName());
	protected ServletSpawner servletSpawner = new ServletSpawner();
	protected static float pin;
	
	public Server(int port, int timeout) {
		this.port = port;
		this.timeout = timeout;
		try {
			server = new ServerSocket(port);
			server.setSoTimeout(timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void protect(float pin){
		Server.pin = pin;
	}
		
	
	@Override
	public void run() {
		while(!Thread.interrupted() && !shutdown){
			try {
				LOG.info("Listening for Clients at " + InetAddress.getLocalHost().toString() + " port " + port);
				Socket client = server.accept();
				servletSpawner.spawn(client);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static class Servlet extends Thread{
		
		DataInputStream inputStream;
		DataOutputStream outputStream;
		Socket conn;
		public boolean shutdown = false;
		
		public Servlet(Socket conn){
			this.conn = conn;
			try {
				outputStream = new DataOutputStream(conn.getOutputStream());
				inputStream = new DataInputStream(conn.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			while(!Thread.interrupted() && !shutdown){
				String instr = read();
				System.out.println(instr);
				if(instr.startsWith("chkusr ")){
					String[] toki = instr.split(" ");
					File fd = new File(System.getProperty("user.dir"), "report/" + toki[1] + ".rp");
					boolean b = fd.exists();
					write(Boolean.toString(b));
				} else if(instr.startsWith("repr ")){
					String[] toki = instr.split(" ");
					File fd = new File(System.getProperty("user.dir"), "report/" + toki[1] + ".rp");
					if(!fd.getParentFile().exists()) fd.getParentFile().mkdirs();
					if(!fd.exists())
						try {fd.createNewFile();} catch (IOException e) {e.printStackTrace();}
					String json = read();
					Report rep = GsonForm.from(json, Report.class);
					rep.detect("./cascade_frontal.xml");
					File f = new File(System.getProperty("user.dir")+ File.separator + "report" + File.separator + toki[1] + ".rp");
					try {
						Files.write(Paths.get(f.getAbsolutePath()), GsonForm.to(rep).getBytes());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					write(Boolean.toString(true));
				} else if(instr.startsWith("view ")){
					String[] toki = instr.split(" ");
					float pin = Float.parseFloat(toki[1]);
					if(pin != Server.pin){ write(Boolean.toString(false)); }
					else{
						File f = new File("./report/" + toki[2] + ".rp");
						try {
							write(new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), StandardCharsets.UTF_8));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else if(instr.startsWith("note ")){
					String[] toki = instr.split(" ");
					float pin = Float.parseFloat(toki[1]);
					if(pin != Server.pin){write(Boolean.toString(false));}
					else {
						String note = read();
						String patient = toki[2];
						File f = new File(System.getProperty("user.dir") + File.separator + "notes" + File.separator + patient + ".html");
						if(!f.getParentFile().exists()) f.getParentFile().mkdirs();
						if(!f.exists())
							try {
								f.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						try {
							Files.write(Paths.get(f.getAbsolutePath()), note.getBytes(StandardCharsets.UTF_8));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						write(Boolean.toString(true));
					}
				} else if(instr.startsWith("gnote ")){
					String[] toki = instr.split(" ");
					File f = new File(System.getProperty("user.dir") + File.separator + "notes" ,toki[1] + ".html");
					if(!f.exists()) write("<p>Your Report has not yet Arrived <br/> We are sorry for the inconvenience</p>");
					else
						try {
							write(new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), StandardCharsets.UTF_8));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				} else if(instr.startsWith("avrpr ")){
					String[] toki = instr.split(" ");
					float inpin = Float.parseFloat(toki[1]);
					if(inpin != Server.pin){
						write(GsonForm.to(new String[]{}));
					} else {
						String[] patient_list = new File(System.getProperty("user.dir"), "report").list();
						write(GsonForm.to(patient_list));
					}
				}
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
		
	}
	
	public static class ServletSpawner extends HashMap<String, Servlet>{

		private static final long serialVersionUID = 1L;
		
		public Servlet spawn(Socket conn){
			Servlet servlet = new Servlet(conn);
			String uuid = UUID.randomUUID().toString();
			LOG.info("Spawning Servlet " + uuid + " for " + conn.toString());
			servlet.start();
			put(uuid, servlet);
			return servlet;
		}
		
		public void repair(){
			for(Entry<String, Servlet> entry : entrySet()){
				String uuid = entry.getKey();
				Servlet servlet = entry.getValue();
				if(servlet.shutdown){
					servlet.cleanup();
					LOG.info("Cleaned Servlet " + uuid);
				}
				remove(entry.getKey(), entry.getValue());
			}
		}
		
		@Override
		public void clear() {
			LOG.info("Clearing Servlets!");
			super.clear();
		}
		
	}
	
}
