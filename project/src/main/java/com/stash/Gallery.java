package com.stash;
import java.util.ArrayList;

public class Gallery {
	// DB Read Vars
	private String id;
	private String path;
	private String checksum;
	private boolean zip;
	private String title;
	private String url;
	private String date;
	private String details;
	private String studio_id;
	private int rating;
	private boolean organized;
	private String created_at;
	private String updated_at;
	private String folderId;
	
	// Calculated vars
	private ArrayList<String> imageIds;
	
	public Gallery(String id, String folderId, String title, String url, String date,
			String details, String studio_id, int rating, boolean organized, String created_at, String updated_at) {
		super();
		this.id = id;
		this.folderId = folderId;
		this.title = title;
		this.url = url;
		this.date = date;
		this.details = details;
		this.studio_id = studio_id;
		this.rating = rating;
		this.organized = organized;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}
	
	public void setImageIds(ArrayList<String> imageids) {
		imageIds = imageids;
	}
	
	public ArrayList<String> getImageIds() {
		return imageIds;
	}

	public String getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	public String getChecksum() {
		return checksum;
	}

	public boolean isZip() {
		return zip;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

	public String getDate() {
		return date;
	}

	public String getStudio_id() {
		return studio_id;
	}
	
	public String getDetails() {
		return details;
	}

	public int getRating() {
		return rating;
	}

	public boolean isOrganized() {
		return organized;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setPath(String p) {
		path = p;
	}
}
