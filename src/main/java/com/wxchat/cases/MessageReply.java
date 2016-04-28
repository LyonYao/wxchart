package com.wxchat.cases;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.wxchat.SslRequestExecutor;
import com.wxchat.cases.TokenManager.Token;
import com.wxchat.data.MessageType;
import com.wxchat.data.TextData;
import com.wxchat.servlets.PostData;

public abstract class MessageReply {

	public static String reply(String key,PostData data){
		if("文本".equals(key)){
			return replyText(data);
		}
		if("图片".equals(key)){
			replyImage(data);
			TextData textData=new TextData();
			textData.setContent("正在上传临时图片，稍后将发送图片...");
			textData.setFromUserName(data.getToUserName());
			textData.setToUserName(data.getFromUserName());
			xstream.alias("xml", textData.getClass());
			return xstream.toXML(textData);
		}
		if("material".equals(key)){
			ScheduledExecutor.getExecutor().execute(new Runnable() {
				
				@Override
				public void run() {
					TemporaryMaterialManager.uploadTemporaryMaterial();
				}
			});
			TextData textData=new TextData();
			textData.setContent("临时图片已经上传.");
			textData.setFromUserName(data.getToUserName());
			textData.setToUserName(data.getFromUserName());
			xstream.alias("xml", textData.getClass());
			return xstream.toXML(textData);
			
		}
		if("token".equals(key)){
			TokenManager instance = TokenManager.getInstance();
			Token token = instance.getToken();
			TextData textData=new TextData();
			textData.setContent(token.getAccessToken());
			textData.setFromUserName(data.getToUserName());
			textData.setToUserName(data.getFromUserName());
			xstream.alias("xml", textData.getClass());
			return xstream.toXML(textData);
		}
		return "";
	}

	private static void replyImage(final PostData data) {
		ScheduledExecutor.getExecutor().execute(new Runnable() {
			@Override
			public void run() {
				String mediaId = TemporaryMaterialManager.uploadTemporaryMaterial();
				if(mediaId!=null){
					HttpPost httppost = new HttpPost("https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token="+TokenManager.getInstance().getToken().getAccessToken());
					Map<String,Object> wdata=new HashMap<String, Object>();
					wdata.put("touser", data.getFromUserName());
					wdata.put("msgtype", MessageType.IMAGE.getType());
					Map<String,Object> inner=new HashMap<String, Object>();
					inner.put("media_id", mediaId);
					wdata.put(MessageType.IMAGE.getKey(), inner);
					ObjectMapper wMapper=new ObjectMapper();
					try {
						httppost.setEntity(new StringEntity(wMapper.writeValueAsString(wdata),Charset.forName("UTF-8")));
						CloseableHttpResponse executePost = new SslRequestExecutor().executePost(httppost);
						
					} catch (KeyManagementException e) {
						e.printStackTrace();
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (KeyStoreException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		
	}

	private static String replyText(PostData data) {
		TextData textData=new TextData();
		textData.setContent("您输入了文本.");
		textData.setFromUserName(data.getToUserName());
		textData.setToUserName(data.getFromUserName());
		xstream.alias("xml", textData.getClass());
		return xstream.toXML(textData);
	}
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				boolean cdata = true;

				@Override
				public String escapeXmlName(String name) {
					return name;
				}

				@SuppressWarnings("unchecked")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});
}
