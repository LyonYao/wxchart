package com.wxchat.data;

public enum MessageType {
	TEXT("text","text"),IMAGE("image","image");
	private String type;
	private String key;
	private MessageType(String type,String key) {
		this.type = type;
		this.key = key;
	}
	public String getType() {
		return type;
	}
	public String getKey() {
		return key;
	}

	

}
