package io.mdk.net.utils;

import java.lang.reflect.Type;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor;
import org.jasypt.salt.RandomSaltGenerator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ning.compress.lzf.LZFDecoder;
import com.ning.compress.lzf.LZFEncoder;
import com.ning.compress.lzf.LZFException;

public class Commons {

	public static class GsonForm {
		
		static final Gson gson = new GsonBuilder().serializeNulls().enableComplexMapKeySerialization().create();
		
		public static String to(Object src){
			return gson.toJson(src);
		}
		
		public static <T> T from(String json, Class<T> typeof){
			return gson.fromJson(json, typeof);
		}

		public static <T> T from(String json, Type type) {
			return gson.fromJson(json, type);
		}
		
	}
	
	public static class Crypto {
		
		private static StandardPBEByteEncryptor encryptor = new StandardPBEByteEncryptor();
		
		static {
			encryptor.setPassword("TX34QZkDvpJfJxpXvpMvhZaLHpsnWVvRkacEKDSf5qkWAXgY");
			encryptor.setSaltGenerator(new RandomSaltGenerator());
		}
		
		public static byte[] encrypt(byte[] in){
			return encryptor.encrypt(in);
		}
		
		public static byte[] decrypt(byte[] in){
			return encryptor.decrypt(in);
		}
		
	}
	
	public static class NLZF {
		
		public static byte[] b_cmpr(byte[] in){
			return LZFEncoder.encode(in);
		}
		
		public static byte[] b_dcmpr(byte[] in){
			byte[] dat = null;
			try {
				dat = LZFDecoder.decode(in);
			} catch (LZFException e) {
				Logger.getLogger("LZF").log(Level.SEVERE, null, e);
			}
			return dat;
		}
		
	}
	
}
