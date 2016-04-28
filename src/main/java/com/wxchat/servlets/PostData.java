package com.wxchat.servlets;

public class PostData {
	private String fromUserName;
	private String toUserName;
	
	public PostData(String fromUserName, String toUserName) {
		super();
		this.fromUserName = fromUserName;
		this.toUserName = toUserName;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	
}
