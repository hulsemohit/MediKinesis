package io.mdk.net.utils;

import com.google.gson.annotations.SerializedName;

public class Report {

	@SerializedName("ImageDat")
	protected final byte[][] imgs;
	
	@SerializedName("face_detected")
	protected byte[] detected;
	
	@SerializedName("Gender")
	public final boolean gender;
	
	@SerializedName("symptoms")
	public final String[] truesymptoms;
	
	@SerializedName("synop")
	public final String synopsis;
	
	/**
	 * Gender indicating Flag
	 */
	public int age;
	// TRUE = MALE, FALSE = FEMALE

	public Report(byte[][] imgs, boolean gender, String[] truesymptoms, int age, String synopsis) {
		super();
		this.imgs = imgs;
		this.gender = gender;
		this.truesymptoms = truesymptoms;
		this.age = age;
		this.synopsis = synopsis;
	}

	public byte[] getDetected() {
		return detected;
	}

	public void setDetected(byte[] detected) {
		this.detected = detected;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public byte[][] getImgs() {
		return imgs;
	}

	public boolean isGender() {
		return gender;
	}

	public String[] getTruesymptoms() {
		return truesymptoms;
	}
	
}
