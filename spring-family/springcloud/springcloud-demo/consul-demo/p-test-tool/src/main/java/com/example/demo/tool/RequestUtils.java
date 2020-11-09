package com.example.demo.tool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

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
		if(responseCode != HttpURLConnection.HTTP_OK){
			InputStream errorStream = connection.getErrorStream();
			InputStreamReader inputStreamReader = new InputStreamReader(errorStream,Charset.forName("GBK"));
	        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
	        String line = null;
	        try{
	            while((line = bufferedReader.readLine())!=null){
	            	 log.error(line);
	            }
	        }catch (Throwable e){
	            log.warn("anaysis response fail.",e);
	        }finally{
	        	if(null != errorStream){
	        		IOUtils.closeQuietly(errorStream);
	        	}
	        	if(null != inputStreamReader){
	        		IOUtils.closeQuietly(inputStreamReader);
	        	}
	        	if(null != bufferedReader){
	        		IOUtils.closeQuietly(bufferedReader);
	        	}
	        }
		}
		return responseCode == HttpURLConnection.HTTP_OK;
	}
}
