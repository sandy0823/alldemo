package com.example.demo.test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class RequestUtils {
	
	public static boolean sendGetRequest(String url) throws MalformedURLException, IOException {
		final HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
		connection.setConnectTimeout(10000);
		connection.setReadTimeout(60000 * 10);
	//	connection.setAllowUserInteraction(false);
		connection.setRequestMethod("GET");

		connection.addRequestProperty("Accept", "*/*");

	//	connection.setChunkedStreamingMode(8196);
		connection.addRequestProperty("Connection", "keep-alive");

		connection.setDoOutput(true);
		// 连接
		connection.connect();
		// 得到响应码
		int responseCode = connection.getResponseCode();
		log.info("url,response code:"+responseCode);
		return responseCode == HttpURLConnection.HTTP_OK;
	}
}
