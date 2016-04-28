package com.wxchat.data;

import java.util.HashMap;
import java.util.Map;

public class ImageData extends MessageData {
	private Map<String,String> Image=new HashMap<String,String>();
	public ImageData() {
		super("image");
	}
	public String getImageMediaId() {
		return Image.get("MediaId");
	}
	public void setImageMediaId(String mediaId) {
		Image.put("MediaId",mediaId);
	}
	public Map<String, String> getImage() {
		return Image;
	}
	public void setImage(Map<String, String> image) {
		Image = image;
	}
	
	

}
