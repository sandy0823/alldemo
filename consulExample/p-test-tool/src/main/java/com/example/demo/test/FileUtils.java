package com.example.demo.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * 重新application.properties的某个配置项值
 * 
 * @author sandy
 *
 */
@Slf4j
public class FileUtils {

	public static boolean setValue(String propertiesAbPath, String key, String value) {
		log.info("start to read file[{}]", propertiesAbPath);
		Properties pro = new Properties();
		FileInputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStream = new FileInputStream(new File(propertiesAbPath));
			pro.load(inputStream);
			pro.setProperty(key, value);
			fileOutputStream = new FileOutputStream(new File(propertiesAbPath));
			pro.store(fileOutputStream, "");
			return true;
		} catch (FileNotFoundException e) {
			log.error("load properties file error.", e);
		} catch (IOException e) {
			log.error("load properties file error.", e);
		} finally {
			if (null != inputStream) {
				IOUtils.closeQuietly(inputStream);
			}
			if (null != fileOutputStream) {
				IOUtils.closeQuietly(fileOutputStream);
			}
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	public static void writeResult(List<Result> results, String filePath,int threadCounts,
			String consulPoolConnections,int connectTimeout) throws IOException {
		File file = new File(filePath);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		try {
			if (!file.exists()) {
				file.createNewFile();
				fos = new FileOutputStream(file);
			} else {
				fos = new FileOutputStream(file, true);
			}
			osw = new OutputStreamWriter(fos, "utf-8");
			osw.write(new Date().toLocaleString()+" "+threadCounts+" " + consulPoolConnections + " " +connectTimeout);
			osw.write("\r\n"); // 换行
			for(Result result:results){
				osw.write(""+result.getThreadCount()+" "+result.getSuccessCount()+" "+result.getErrorCount()+ " "+((float)result.getSuccessCount())/result.getTakeTime()); // 写入内容
				osw.write("\r\n"); // 换行
			}
		} finally {
			if (osw != null) {
				IOUtils.closeQuietly(osw);
			}
			if (fos != null) {
				IOUtils.closeQuietly(fos);
			}
		}
	}

}
