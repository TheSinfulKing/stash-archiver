package com.stash;

import java.util.ArrayList;

public class Tag {
	// DB Read
	private String id;
	private String name;
	
	// Calculate
	private ArrayList<String> childrenIds;
	
	public Tag(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setChildrenId(ArrayList<String> ids) {
		childrenIds = ids;
	}
	
	public String[] getChildrenIds() {
		if(childrenIds == null) {
			return null;
		}
		return childrenIds.toArray(new String[0]);
	}
}
