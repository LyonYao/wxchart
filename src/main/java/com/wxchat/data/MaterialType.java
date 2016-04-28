package com.wxchat.data;

public enum MaterialType {
	IMAGE("image");
	private String type;

	private MaterialType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return type;
	}
	
}
