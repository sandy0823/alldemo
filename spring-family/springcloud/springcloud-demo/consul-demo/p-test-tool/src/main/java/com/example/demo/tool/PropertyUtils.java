package com.example.demo.tool;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import com.example.demo.test.annotation.ConfigKey;
import com.example.demo.test.model.Config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropertyUtils {
	/****
	 * config 目录与工具jar包同级，有优先读取该config/application.properties文件， 从而让使用者可以自定义值
	 */
	private final static String CONFIG_DIR = "config";
	private final static String APPLICATION_PROPERTIES = "application.properties";

	/**
	 * 优先从与jar同级目录下的config/application.properties中获取配置，
	 * 当获取不到时，从jar包中的application.properties文件中读取，
	 * 当application.properties中的某个配置项也无值时，则使用代码中的默认值
	 * 
	 * @return
	 */
	public static Config loadConfig() {
		Properties proFromClass = loadFromClass();
		Properties proFromConfigDir = loadFromConfigDir();

		proFromClass.forEach((key, value) -> {
			if (proFromConfigDir.containsKey(key)
					&& StringUtils.isNotBlank(proFromConfigDir.getProperty((String) key))) {
				proFromClass.put(key, proFromConfigDir.getProperty((String) key));
			}
		});

		// via reflect,init Object of Config
		Config config = new Config();
		Field[] fields = config.getClass().getDeclaredFields();

		Stream.of(fields).forEach(field -> {
			field.setAccessible(true);
			ConfigKey configKey = field.getAnnotation(ConfigKey.class);
			String key = configKey.value();
			if (StringUtils.isNotEmpty(key) && proFromClass.containsKey(key)) {
				String value = proFromClass.getProperty(key);
				if (StringUtils.isNotEmpty(value)) {
					try {
						String type = field.getType().toString();//得到此属性的类型
						if(type.endsWith("String")){
							field.set(config, value);
						}else if(type.endsWith("int")){
							field.set(config, Integer.valueOf(value));
						}else if(type.endsWith("boolean")){
							field.set(config, Boolean.valueOf(value));
						}else{
							field.set(config, value);
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						log.error("cannot set config value to object of Config class",e);
					}
				}
			}
		});
		return config;
	}

	private static Properties loadFromConfigDir() {
		try {
			String path = PropertyUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile();
			path = java.net.URLDecoder.decode(path, "UTF-8");
			log.info("jar path is [{}]", path);
			int index = path.lastIndexOf("/");
			if(index == -1){
				index = path.lastIndexOf("\\");
			}
			int lastIndex = index + 1;
			String osName = System.getProperty("os.name");
			if(osName.toLowerCase().contains("windows")){  //windows 平台
				path = path.substring(1, lastIndex);
			}else if(osName.toLowerCase().contains("linux")){
				path = path.substring(0, lastIndex);
			}
			
			path = StringUtils.joinWith(File.separator, path, CONFIG_DIR, APPLICATION_PROPERTIES);
			log.info("config dir path is [{}]", path);
			return loadProperties(new FileInputStream(new File(path)));
		} catch (UnsupportedEncodingException e) {
			log.error("cannot get jar path", e);
		} catch (FileNotFoundException e) {
			log.warn("cannot get application.properties in config dir", e);
		}
		return new Properties() ;
	}

	private static Properties loadFromClass() {
		try {
			return loadProperties(PropertyUtils.class.getClassLoader().getResourceAsStream(APPLICATION_PROPERTIES));
		} catch (Throwable ex) {
			log.error("cannot load application.properties from context of class", ex);
		}
		return new Properties() ;
	}

	private static Properties loadProperties(InputStream inputStream) {
		Properties pros = new Properties();
		try {
			if (null != inputStream) {
				pros.load(inputStream);
				return pros;
			}

		} catch (Throwable ex) {
			log.error("cannot load inputstream", ex);
		} finally {
			if (inputStream != null) {
				IOUtils.closeQuietly(inputStream);
			}
		}
		return pros;
	}
}
