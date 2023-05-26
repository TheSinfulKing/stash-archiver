package com.stash;
import java.time.LocalDate;

public class Performer {
	// DB Read
	private String name;
	private String stashid;
	//private String checksum;
	private LocalDate birthdate;
	private String ethnicity;
	private String country;
	private String eye_color;
	private String height;
	private String measurements;
	private String fake_tits;
	private boolean favorite;
	private String death_date;
	private String hair_color;
	private String weight;
	private int rating;
	private String url;
	private String created_at;
	private String updated_at;
	
	// Calculated
	private String[] tagIds;

	public Performer(String name, String stashid, LocalDate birthdate, String ethnicity,
			String country, String eye_color, String height, String measurements, String fake_tits, boolean favorite,
			String death_date, String hair_color, String weight, int rating, String url, String created_at, String updated_at) {
		super();
		this.name = name;
		this.stashid = stashid;
		//this.checksum = checksum;
		this.birthdate = birthdate;
		this.ethnicity = ethnicity;
		this.country = country;
		this.eye_color = eye_color;
		this.height = height;
		this.measurements = measurements;
		this.fake_tits = fake_tits;
		this.favorite = favorite;
		this.death_date = death_date;
		this.hair_color = hair_color;
		this.weight = weight;
		this.rating = rating;
		this.url = url;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

	public String getName() {
		return name;
	}

	public String getStashid() {
		return stashid;
	}

	// public String getChecksum() {
	// 	return checksum;
	// }

	public LocalDate getBirthdate() {
		return birthdate;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public String getCountry() {
		return country;
	}

	public String getEye_color() {
		return eye_color;
	}

	public String getHeight() {
		return height;
	}

	public String getMeasurements() {
		return measurements;
	}

	public String getFake_tits() {
		return fake_tits;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public String getDeath_date() {
		return death_date;
	}

	public String getHair_color() {
		return hair_color;
	}

	public String getWeight() {
		return weight;
	}

	public int getRating() {
		return rating;
	}

	public String getUrl() {
		return url;
	}
	
	public void setTagIds(String[] ids) {
		tagIds = ids;
	}
	
	public String[] getTagIds() {
		return tagIds;
	}

	public String toString() {
		String ret = "";
		
		ret += getName().toUpperCase();

//		ret += getName().toUpperCase();
//		ret += ": ";
//		ret += getStashid();
//		ret += " - ";
//		ret += getChecksum();
//		ret += " - ";
//		ret += getGender();
//		ret += " - ";
//		ret += getBirthdate();
//		ret += " - ";
//		ret += getEthnicity();
//		ret += " - ";
//		ret += getCountry();
//		ret += " - ";
//		ret += getEye_color();
//		ret += " - ";
//		ret += getHeight();
//		ret += " - ";
//		ret += getMeasurements();
//		ret += " - ";
//		ret += getFake_tits();
//		ret += " - ";
//		ret += isFavorite();
//		ret += " - ";
//		ret += getDeath_date();
//		ret += " - ";
//		ret += getHair_color();
//		ret += " - ";
//		ret += getWeight();
//		ret += " - ";
//		ret += getRating();
//		ret += " - ";
//		ret += getUrl();

		return ret;
	}
}
