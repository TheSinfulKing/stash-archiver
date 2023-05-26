package com.stash;
import java.util.ArrayList;

public class Studio {
	// DB Read
	private String id;
	private String parent_id;
	private String checksum;
	private String name;
	private String url;
	private String details;
	private int rating;
	
	// Calculate
	private ArrayList<String> childrenIds;
	private ArrayList<String> galleryIds;
	private ArrayList<String> imageIds;
	private int galleryCount = 0;
	private int imageCount = 0;
	
	public Studio(String id, String checksum, String name, String url, String parentId, String details, int rating) {
		super();
		this.id = id;
		this.parent_id = parentId;
		this.checksum = checksum;
		this.name = name;
		this.url = url;
		this.details = details;
		this.rating = rating;
	}

	public String getId() {
		return id;
	}
	
	public String getParentId() {
		return parent_id;
	}

	public String getChecksum() {
		return checksum;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getDetails() {
		return details;
	}

	public int getRating() {
		return rating;
	}
	
	public void addChildId(String id) {
		if(childrenIds == null) {
			childrenIds = new ArrayList<String>();
		}
		childrenIds.add(id);
	}
	
	public ArrayList<String> getChildrenIds() {
		if(childrenIds == null) {
			return null;
		}
		return childrenIds;
	}
	
	public void addGalleryId(String id) {
		if(galleryIds == null) {
			galleryIds = new ArrayList<String>();
		}
		galleryIds.add(id);
		galleryCount++;
	}
	
	public void addImageId(String id) {
		if(imageIds == null) {
			imageIds = new ArrayList<String>();
		}
		imageIds.add(id);
		imageCount++;
	}
	
	public int getGalleryCount() {
		return galleryCount;
	}
	
	public int getImageCount() {
		return imageCount;
	}
}
