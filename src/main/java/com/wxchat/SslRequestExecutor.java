package com.wxchat;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class SslRequestExecutor {
	private final CloseableHttpClient httpclient;
	
	public SslRequestExecutor() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
				null, new TrustStrategy() {
					// 信任所有
					public boolean isTrusted(X509Certificate[] chain,
							String authType) throws CertificateException {
						return true;
					}
				}).build();
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext);
		httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
	}
	public SslRequestExecutor(SSLContext sslContext) {
		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
				sslContext);
		httpclient = HttpClients.custom()
				.setSSLSocketFactory(sslsf).build();
	}
	public CloseableHttpResponse executePost(HttpPost httppost) throws ClientProtocolException, IOException{
		return httpclient.execute(httppost);
	}
}
