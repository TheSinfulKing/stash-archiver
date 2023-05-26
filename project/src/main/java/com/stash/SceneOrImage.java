package com.stash;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

enum Type {
	SCENE, IMAGE
}

public class SceneOrImage {
	private Type type;
	private Scene scene;
	private Image image;

	public SceneOrImage(Scene scene) {
		this.scene = scene;
		type = Type.SCENE;
	}

	public SceneOrImage(Image image) {
		this.image = image;
		type = Type.IMAGE;
	}

	public boolean isScene() {
		if(type == Type.SCENE) {
			return true;
		}
		return false;
	}
	
	public boolean isImage() {
		if(type == Type.SCENE) {
			return false;
		}
		return true;
	}

	public boolean shouldBeTreatedAsVideo() {
		if(type == Type.SCENE) {
			return true;
		}
		return image.ShouldBeTreatedAsVideo();
	}

	public String getStashId() {
		switch (type) {
		case SCENE:
			return scene.getStashId();
		case IMAGE:
			return image.getStashId();
		default:
			return null;
		}
	}
	
	public String[] getPerformerIds() {
		switch(type) {
		case SCENE:
			return scene.getPerformerIds();
		case IMAGE:
			return image.getPerformerIds();
		}
		return null;
	}
	
	public String[] getTagIds() {
		switch(type) {
		case SCENE:
			return scene.getTagIds();
		case IMAGE:
			return image.getTagIds();
		}
		return null;
	}
	
	public String getChecksum() {
		switch(type) {
		case SCENE:
			return scene.getChecksum();
		case IMAGE:
			return image.getChecksum();
		}
		return null;
	}
	
	public int getRating() {
		if(type == Type.IMAGE) {
			return image.getRating();
		}
		return scene.getRating();
	}

	public String getFilename() {
		if(type == Type.IMAGE) {
			return image.getFilename();
		}
		return scene.getFilename();
	}
	
	public String getHash() {
		if(type == Type.IMAGE) {
			// img doesn't have oshash
			System.err.println("Image hash is trying to be accessed, but images don't have hashes!");
			return null;
		}
		return scene.getOshash();
	}
	
	public boolean isOrganized() {
		if(type == Type.IMAGE) {
			return image.isOrganized();
		}
		return scene.isOrganized();
	}
	
	public String getPath() {
		if(type == Type.IMAGE) {
			return image.getPath();
		}
		return scene.getPath();
	}
	
	public LocalDate getDate() {
		if(type == Type.IMAGE) {
			// img doesn't have date
			System.err.println("Image date is trying to be accessed, but images don't have dates!");
			return null;
		}
		return scene.getDate();
	}
	
	public String getPHash() {
		if(type == Type.IMAGE) {
			// img doesn't have date
			System.err.println("Image phash is trying to be accessed, but images don't have phashes!");
			return null;
		}
		return scene.getPhash();
	}
	
	public int getResolution() {
		if(type == Type.IMAGE) {
			return image.getDimensions().getHeight();
		}
		return scene.getDimensions().getHeight();
	}

	public LocalDateTime getCreatedAtDateTime() {
		if(type == Type.IMAGE) {
			return image.getCreatedAtDateTime();
		}
		return scene.getCreatedAtDateTime();
	}

	public LocalDateTime getUpdatedAtDateTime() {
		if(type == Type.IMAGE) {
			return image.getUpdatedAtDateTime();
		}
		return scene.getUpdatedAtDateTime();
	}
	
	public String getStudioId() {
		if(type == Type.IMAGE) {
			return image.getStudio_id();
		}
		return scene.getStudio_id();
	}
	
	public String getDetails() {
		if(isImage()) {
			return null;
		}
		return scene.getDetails();
	}
	
	public int hashCode() {
		if(type == Type.IMAGE) {
			return image.hashCode();
		}
		return scene.hashCode();
	}

	public int getWidth() {
		if(type == Type.IMAGE) {
			return image.getDimensions().getWidth();
		}
		else {
			return scene.getDimensions().getWidth();
		}
	}

	public int getHeight() {
		if(type == Type.IMAGE) {
			return image.getDimensions().getHeight();
		}
		else {
			return scene.getDimensions().getHeight();
		}
	}
	
	public String getUrl() {
		if(type == Type.IMAGE) {
			System.err.println("Image URL is trying to be accessed, but images don't have URLs!");
			return null;
		}
		return scene.getUrl();
	}
	
	public String getTitle() {
		if(type == Type.IMAGE) {
			return image.getTitle();
		}
		return scene.getTitle();
	}
	
	public int getOCounter() {
		if(type == Type.IMAGE) {
			return image.getO_counter();
		}
		return scene.getO_counter();
	}
	
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		}
		if(this == other) { return true; }
		if(other instanceof SceneOrImage) {
			SceneOrImage o2 = (SceneOrImage) other;
			if(type == Type.IMAGE) {
				if(o2.type != Type.IMAGE) {
					return false;
				}
				return this.image.equals(o2.image);
			}
			if(type == Type.SCENE) {
				if(o2.type != Type.SCENE) {
					return false;
				}
				return this.scene.equals(o2.scene);
			}
		}
		return false;
	}
}
