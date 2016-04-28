package com.wxchat.cases;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxchat.result.MpTokenResult;

/**
 * @author lyon
 *
 */
public class TokenManager {
	private final static TokenManager INSTANCE=new TokenManager();
	private final static Logger LOGGER=Logger.getLogger(TokenManager.class);
	private String appId="wx0594ed1acbd0dd0d";
	private String secret="8020134a43a47bcd48b0527f104d1d06";
	private Token token=null;
	private TokenManager() {
		super();
	}

	public static TokenManager getInstance(){
		return INSTANCE;
	}
	
	public synchronized Token getToken(){
		if(token==null||token.getExpireDate().before(new Date())){
			requestNewToken();
		}
		return token;
	}
	private void requestNewToken() {
	    final String url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+appId+"&secret="+secret;
		CloseableHttpClient httpclient = HttpClients.custom().build();
		HttpGet get=new HttpGet(url);
		get.addHeader("Connection", "keep-alive");
		get.addHeader("Accept", "*/*");
		get.addHeader("Content-Type",
				"application/x-www-form-urlencoded; charset=UTF-8");
		try {
			CloseableHttpResponse response = httpclient.execute(get);
			InputStream content = response.getEntity().getContent();
			MpTokenResult tokenResult = new ObjectMapper().readValue(content, MpTokenResult.class);
			LOGGER.info(token);
			if(tokenResult.getErrcode()==0){
				Calendar c = Calendar.getInstance();
				c.add(Calendar.SECOND, tokenResult.getExpires_in()-3);//提前3秒过期
				token=new Token(tokenResult.getAccess_token(), c.getTime());
			}else{
				LOGGER.error(String.format("Get token error,code:%d,msg:%s",new Object[]{tokenResult.getErrcode(),tokenResult.getErrmsg()}));
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static class Token{
		private String accessToken;
		private Date expireDate;
		public String getAccessToken() {
			return accessToken;
		}
		public void setAccessToken(String accessToken) {
			this.accessToken = accessToken;
		}
		public Date getExpireDate() {
			return expireDate;
		}
		public void setExpireDate(Date expireDate) {
			this.expireDate = expireDate;
		}
		private Token(String accessToken, Date expireDate) {
			super();
			this.accessToken = accessToken;
			this.expireDate = expireDate;
		}
		
	}
	
}
