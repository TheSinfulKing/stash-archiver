package com.stash;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Scene {
/*
 * Lookup Vars
 * These are read directly from Stash database
 */	
	// `scenes` table - base set of vars
	private String id;
	private String title;
	private String url;
	private LocalDate date;
	private int rating;
	private String studio_id;
	private int o_counter;
	private boolean organized;
	private String details;
	private LocalDateTime created_at;
	private LocalDateTime updated_at;
	
	// `scenes_files` table
	private String fileId;
	
	// `video_files` table
	private double duration;
	private String format;
	private String video_codec;
	private String audio_codec;
	private Dimensions dimensions;
	private String framerate;
	private String bitrate;
	
	// `files` table + `folders` table
	private String basename; // filename
	private String folderpath; // folder path
	private String folderId;
	private String size;
	
	// `files_fingerprints` table
	private String checksum;
	private String oshash;
	private String phash;
	
/*--------------------END LOOKUP VARS-------------------- */
	
/*
 * Calculated Vars
 * These are calculated using instance vars and more lookups
 */
	// performer ids are fetched from `performers_scenes`, then set here
	private String[] performer_ids;
	// tag ids are fetched from `scenes_tags`, then set here. 
	private String[] tag_ids;
/*--------------------END CALCULATED VARS-------------------- */
	
	
	public Scene(String stashId, String title, String url, LocalDate date,
			int rating, String studio_id, int o_counter, boolean organized, String details, LocalDateTime created, LocalDateTime updated) {
		super();
		this.id = stashId;
		this.title = title;
		this.url = url;
		this.date = date;
		this.rating = rating;
		this.studio_id = studio_id;
		this.o_counter = o_counter;
		this.organized = organized;
		this.details = details;
		this.created_at = created;
		this.updated_at = updated;
	}

/*--------------------- GETTERS ------------------------------- */
	public String getStashId() {
		return id;
	}
	public String getPath() {
		if(folderpath == null) {
			System.err.println("folderpath is null for Scene Id " + id);
		}
		if(basename == null) {
			System.err.println("basename is null for Scene Id " + id);
		}
		return folderpath + "\\" + basename;
	}
	public String getChecksum() {
		return checksum;
	}
	public String getOshash() {
		return oshash;
	}
	public String getTitle() {
		return title;
	}
	public String getUrl() {
		return url;
	}
	public LocalDate getDate() {
		return date;
	}
	public int getRating() {
		return rating;
	}
	public String getSize() {
		return size;
	}
	public double getDuration() {
		return duration;
	}
	public String getVideo_codec() {
		return video_codec;
	}
	public String getAudio_codec() {
		return audio_codec;
	}
	public Dimensions getDimensions() {
		return dimensions;
	}
	public String getFramerate() {
		return framerate;
	}
	public String getBitrate() {
		return bitrate;
	}
	public String getStudio_id() {
		return studio_id;
	}
	public int getO_counter() {
		return o_counter;
	}
	public String getFormat() {
		return format;
	}
	public boolean isOrganized() {
		return organized;
	}
	public String getPhash() {
		return phash;
	}
	public String getPerformersIdString() {
		if(performer_ids == null) {
			return "NULL[performer_ids])";
		}
		String ret = "";
		for(int i=0;i<performer_ids.length;i++) {
			ret += performer_ids[i];
			if(i != performer_ids.length-1) {
				ret += ", ";
			}
		}
		return ret;
	}	
	public String[] getPerformerIds() {
		return performer_ids;
	}	
	public String[] getTagIds() {
		return tag_ids;
	}	
	public String getDetails() {
		return details;
	}	
	public String getFileId() {
		return fileId;
	}
	public String getFolderId() {
		return folderId;
	}
	public String[] getTagsIds() {
		return tag_ids;
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

/*--------------------- SETTERS ------------------------------- */
	public void setPerformers(String[] in) {
		performer_ids = in;
	}	
	public void setFileId(String f) {
		fileId = f;
	}
	public void setVideoFilesVars(double d, String form, String vc, String ac, Dimensions dm, String f, String b) {
		duration = d;
		format = form;
		video_codec = vc;
		audio_codec = ac;
		dimensions = dm;
		framerate = f;
		bitrate = b;
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
	public void setHash(String h) {
		oshash = h;
	}
	public void setPHash(String ph) {
		phash = ph;
	}
	public void setTagIds(String[] tagIds) {
		this.tag_ids = tagIds;
	}

/*--------------------- OTHER ------------------------------- */	
	public String toString() {
		String ret = "";
		
		ret += getStashId();
		
//		ret += getStashid() + ": ";
//		ret += getPath() + " - ";
//		ret += getChecksum() + " - ";
//		ret += getOshash() + " - ";
//		ret += getTitle() + " - ";
//		ret += getUrl() + " - ";
//		ret += getDate() + " - ";
//		ret += getRating() + " - ";
//		ret += getSize() + " - ";
//		ret += getDuration() + " - ";
//		ret += getVideo_codec() + " - ";
//		ret += getAudio_codec() + " - ";
//		ret += getDimensions() + " - ";
//		ret += getFramerate() + " - ";
//		ret += getBitrate() + " - ";
//		ret += getStudio_id() + " - ";
//		ret += getO_counter() + " - ";
//		ret += getFormat() + " - ";
//		ret += isOrganized() + " - ";
//		ret += getPhash() + " - ";
//		ret += getPerformersString();
		
		return ret;
	}
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
		
		if(other instanceof Scene) {
			Scene o2 = (Scene) other;
			return o2.id.equalsIgnoreCase(o2.id);
		}
		return false;
	}
}
