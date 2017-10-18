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
		this.unit = unit;
	}

	public YQW submit(ExHandler handler){
		YQW ret = null;
		try {
			String URLname = URLEncoder.encode(locality, "UTF-8");
			// These are 3 YQL queries that will be sent to the Yahoo weather API
			URL www = new URL("https://query.yahooapis.com/v1/public/yql?q="
					+ "select%20item.condition%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+URLname+"%22)%20and%20u%3D'"
					+unit+"'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
			URL www2 = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20astronomy%2Cwind%2Catmosphere%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"
					+ URLname + "%22)%20and%20u%3D'"
					+ unit	+ "'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
			URL www3 = new URL("https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20%3D%200%20and%20u%3D'"+unit+"'&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys");
			//The responses for the queries
			String json = getTextFromURL(www);
			String json2 = getTextFromURL(www2);
			String json3 = getTextFromURL(www3);
			//System.out.println(json2);
			Type type = new TypeToken<QContainer<YQW>>(){}.getType();
			QContainer<YQW> container = GsonForm.from(json, type);
			QContainer<YQW> units_container = GsonForm.from(json3, type);
			QContainer<YQW> wind_container = GsonForm.from(json2, type);
			//System.out.println(wind_container.query.getChill());
			container.query.mergeChannels(units_container.query);
			container.query.mergeChannels(wind_container.query);
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
	
	private static String getTextFromURL(URL url) throws IOException{
		InputStream is = url.openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line; StringBuilder BUILDER = new StringBuilder();
		while((line = br.readLine()) != null){	// Loop until EOF
			BUILDER.append(line);
		}
		return BUILDER.toString();
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
		
		public String getDate(){
			return results.channel.item.condition.date;
		}
		
		public String getChill(){
			return results.channel.wind.chill;
		}
		
		public String getDirection(){
			return results.channel.wind.direction;
		}
		
		public String getSpeed(){
			return results.channel.wind.speed;
		}
		
		public String getSunsetTime(){
			return results.channel.day.sunset;
		}
		
		public String getSunriseTime(){
			return results.channel.day.sunrise;
		}
		
		private void mergeChannels(YQW yqw){
			if(yqw.results.channel.wind != null){
				results.channel.wind = yqw.results.channel.wind;
			}
			if(yqw.results.channel.item != null){
				results.channel.item = yqw.results.channel.item;
			}
			if(yqw.results.channel.units != null){
				results.channel.units = yqw.results.channel.units;
			}
			if(yqw.results.channel.day != null){
				results.channel.day = yqw.results.channel.day;
			}
			if(yqw.results.channel.atData != null){
				results.channel.atData = yqw.results.channel.atData;
			}
		}
		
		public String getDistanceUnit(){
			return results.channel.units.distance;
		}
		
		public String getTemperatureUnit(){
			return results.channel.units.temperature;
		}
		
		public String getPressureUnit(){
			return results.channel.units.pressure;
		}
		
		public String getSpeedUnit(){
			return results.channel.units.speed;
		}
		
		public String getHumidity(){
			return results.channel.atData.humidity;
		}
		
		public String getVisibility(){
			return results.channel.atData.visibility;
		}
	}
	
	public static class RegularResults { WeatherChannel channel;}
	
	public static class WeatherCondition {
		
		String code;
		String date;
		String temp;
		String text;
	
	}
	
	public static class WItem { WeatherCondition condition; }
	public static class WeatherChannel { WItem item; Wind wind; Units units; @SerializedName("astronomy")Daytime day; @SerializedName("atmosphere") AtmosphericData atData;}
	
	public static class Wind {
		String chill;
		String direction;
		String speed;
	}
	
	public static class Units {
		String distance;
		String pressure;
		String speed;
		String temperature;
	}
	
	public static class Daytime {
		
		String sunset;
		String sunrise;
		
	}
	
	public static class AtmosphericData {
		String humidity;
		String visibility;
		String pressure;
	}

	public static class QContainer<T> {

		public T query;
	
	}

	
}
