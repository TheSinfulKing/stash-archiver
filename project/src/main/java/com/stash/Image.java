package com.stash;
import java.time.LocalDateTime;

import com.utils.UTILS;

public class Image {
	// DB Read Vars
	// `images` table
	private String id;
	private String title;
	private int rating;
	private String studio_id;
	private int o_counter;
	private boolean organized;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	// `images_files` table
	private String fileId;
	private String folderId;
	private String format;
	private Dimensions dimensions;
	
	// `files` table
	private String size;
	private String basename;
	private String folderpath;
	
	private String checksum;
	
	// Calculated values
	private String[] performer_ids;
	private String[] tag_ids;
	
	public Image(String id, String title, int rating,
			String studio_id, int o_counter, boolean organized, LocalDateTime created_at, LocalDateTime updated_at) {
		super();
		this.id = id;
		this.title = title;
		this.rating = rating;
		this.studio_id = studio_id;
		this.o_counter = o_counter;
		this.organized = organized;
		this.created_at = created_at;
		this.updated_at = updated_at;
	}

/*------------- GETTERS --------------- */
	public String getStashId() {
		return id;
	}
	public String getPath() {
		return folderpath + "\\" + basename;
	}
	public String getChecksum() {
		return checksum;
	}
	public String getTitle() {
		return title;
	}
	public int getRating() {
		return rating;
	}
	public String getSize() {
		return size;
	}
	public Dimensions getDimensions() {
		return dimensions;
	}
	public String getStudio_id() {
		return studio_id;
	}
	public int getO_counter() {
		return o_counter;
	}
	public boolean isOrganized() {
		return organized;
	}
	public String[] getPerformerIds() {
		return performer_ids;
	}
	public String[] getTagIds() {
		return tag_ids;
	}
	public String getFileId() {
		return fileId;
	}
	public String getFormat() {
		return format;
	}
	public String getFolderId() {
		return folderId;
	}
	public LocalDateTime getCreatedAtDateTime() {
		return created_at;
	}
	public LocalDateTime getUpdatedAtDateTime() {
		return updated_at;
	}
	public String getFilename() {
		return basename;
	}
	
/*------------- SETTERS --------------- */
	public void setPerformers(String[] in) {
		performer_ids = in;
	}
	public void setImageFilesVars(String f, Dimensions d) {
		format = f;
		dimensions = d;
	}
	public void setFilesVars(String s, String b, String fp) {
		size = s;
		basename = b;
		folderpath = fp;
	}
	public void setFileId(String fid) {
		fileId = fid;
	}
	public void setFilesVars1(String base, String fp, String f, String si) {
		basename = base;
		folderpath = fp;
		folderId = f;
		size = si;
	}
	public void setChecksum(String c) {
		checksum = c;
	}
	public void setTagIds(String[] tagIds) {
		this.tag_ids = tagIds;
	}

/*------------- OTHER --------------- */	
	public int hashCode() {
		// https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method-for-a-collection
		int result = 28;
		
		int c = Integer.parseInt(id);
		
		result = 37 * result + c;
		return result;
	}
	public boolean equals(Object other) {
		if(other == null) { return false; }
		if(this == other) { return true; }
		
		if(other instanceof Image) {
			Image o2 = (Image) other;
			return o2.id.equalsIgnoreCase(o2.id);
		}
		return false;
	}

	public boolean ShouldBeTreatedAsVideo() {
		return format.equalsIgnoreCase("GIF") || UTILS.GETEXTENSION(basename).equalsIgnoreCase("GIF");
	}
}
