package com.wxchat.cases;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wxchat.SslRequestExecutor;
import com.wxchat.data.MaterialType;
import com.wxchat.result.MpTemporaryMaterialReult;

public abstract class TemporaryMaterialManager {
	private final static Logger LOGGER = Logger
			.getLogger(TemporaryMaterialManager.class);

	public static String uploadTemporaryMaterial() {
		final String url = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token="
				+ TokenManager.getInstance().getToken().getAccessToken()
				+ "&type=" + MaterialType.IMAGE;
		LOGGER.info("Upload temporary material:" + url);
		try {
			HttpPost httppost = new HttpPost(url);
			InputStream openStream = TemporaryMaterialManager.class
					.getResource("/upload.jpg").openStream();
			ByteArrayOutputStream out=new ByteArrayOutputStream();
			int b=-1;
			while((b=openStream.read())!=-1){
				out.write(b);
			}
			httppost.setEntity(MultipartEntityBuilder
					.create()
					.setMode(HttpMultipartMode.RFC6532)
					.addPart(
							"media",
							new ByteArrayBody(out.toByteArray(),"media.jpg")).build());
			CloseableHttpResponse response = new SslRequestExecutor().executePost(httppost);
			InputStream content = response.getEntity().getContent();
			MpTemporaryMaterialReult result = new ObjectMapper().readValue(
					content, MpTemporaryMaterialReult.class);
			if (result.getErrcode() == 0) {
				LOGGER.info("Uploaded image:" + result);
				return result.getMedia_id();
			} else {
				LOGGER.info("Uploaded image error:" + result.getErrcode()
						+ ",msg:" + result.getErrmsg());
				
			}
		} catch (Exception e) {
			LOGGER.error(e);
			e.printStackTrace();
		}
		return null;

	}
}
