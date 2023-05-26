package com.stash;
public class Dimensions {
	private int width, height;

	public Dimensions(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String toString() {
		return width + "x" + height;
	}
}
