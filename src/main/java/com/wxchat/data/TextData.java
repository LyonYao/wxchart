package com.wxchat.data;

public class TextData extends MessageData {
	public TextData() {
		super( "text");
	}
	private String Content;
	
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	
}
