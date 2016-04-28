package com.wxchat.cases;

import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.wxchat.SslRequestExecutor;

public class MenuPusher {

	public void push() {
		String menu = "{\"button\":[{\"type\":\"view\",\"name\":\"VIew歌曲\",\"url\":\"http://116114.wwz114.cn/wxchat/index.jsp\"},{\"name\":\"菜单\",\"sub_button\":[{\"type\":\"view\",\"name\":\"搜索\",\"url\":\"http://www.soso.com/\"},{\"type\":\"view\",\"name\":\"视频\",\"url\":\"http://v.qq.com/\"},{\"type\":\"click\",\"name\":\"赞一下我们\",\"key\":\"V1001_GOOD\"}]}]}";
		HttpPost httppost = new HttpPost("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="+TokenManager.getInstance().getToken().getAccessToken());
		try {
		httppost.setEntity(new StringEntity(menu,Charset.forName("UTF-8")));
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
