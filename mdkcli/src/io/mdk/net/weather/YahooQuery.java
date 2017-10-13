package io.mdk.net.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import io.mdk.net.client.Client.ExHandler;
import io.mdk.net.utils.Commons.GsonForm;

public class YahooQuery {

	String locality;
	String unit;
	
	public YahooQuery(String locality, String unit) {
		super();
		this.locality = locality;
	}

	public YQW submit(ExHandler handler){
		YQW ret = null;
		try {
			String URLname = URLEncoder.encode(locality, "UTF-8");
			URL www = new URL("https://query.yahooapis.com/v1/public/yql?q="
					+ "select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+URLname+"%22)%20and%20u%3D'"
					+unit+"'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
			InputStream is = www.openStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			StringBuilder builder = new StringBuilder();
			String line;
			while((line = br.readLine()) != null){
				builder.append(line);
			}
			String json = builder.toString();
			Type type = new TypeToken<QContainer<YQW>>(){}.getType();
			QContainer<YQW> container = GsonForm.from(json, type);
			ret = container.query;
		} catch (UnsupportedEncodingException | MalformedURLException e) {
			handler.handle(e);
			e.printStackTrace();
		} catch (IOException e){
			handler.handle(e);
			e.printStackTrace();
		}
		return ret;
	}
	
	public static class YQW {
		
		@SerializedName("created")
		public String timeCreated;
		
		@SerializedName("lang")
		public String langOfDoc;
		RegularResults results;
		
		public String getTemp(){
			return results.channel.item.condition.temp;
		}
		
		public String getDesciptorText(){
			return results.channel.item.condition.text;
		}
	}
	
	public static class RegularResults { WeatherChannel channel; }
	
	public static class WeatherCondition {
		
		String code;
		String date;
		String temp;
		String text;
		
	}
	
	public static class WItem { WeatherCondition condition; }
	public static class WeatherChannel { WItem item; }
	


	public static class QContainer<T> {

		public T query;
	
	}

	
}
