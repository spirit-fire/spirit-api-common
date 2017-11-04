package com.spirit.commons.common.confs;

import com.spirit.commons.common.StringUtils;

import java.io.*;
import java.util.*;

@SuppressWarnings("rawtypes")
public class PropertiesConfigLoader implements ConfigLoader {

	private Properties properties;
	
	/** weibo_conf 配置路径的环境变量名 */
	public static final String SPIRIT_CONF_PATH_VAR = "SPIRIT_CONF_PATH";
	
	/** 默认配置路径 */
	public static final String DEFAULT_SPIRIT_CONF_PATH = "/data1/confs/geek_book";
	
	/** xml 后缀名 */
	public static final String XML_FILE_EXTENSION = ".xml";
	
	/** 配置路径 */
	private String spiritConfPath;
	
	public PropertiesConfigLoader(){
		this(null);
	}
	
	private void info(String msg){
		System.out.println(msg);
	}
	
	public PropertiesConfigLoader(String spiritConfPath){
		this.properties = new Properties();
		
		// 获取外部配置路径
		if(StringUtils.isNullOrEmpty(spiritConfPath)){
			spiritConfPath = System.getenv(SPIRIT_CONF_PATH_VAR);
			// 如果没有设置环境变量则使用默认路径
			if(StringUtils.isNullOrEmpty(spiritConfPath)){
				spiritConfPath = DEFAULT_SPIRIT_CONF_PATH;
			}
		}
		this.spiritConfPath = spiritConfPath;
//		info("spiritConfPath: "+spiritConfPath);
	}
	
	@Override
	public void load() {
		this.loadSystemConfigProperties();
	}

	private void loadSystemConfigProperties(){
		File dir = new File(this.spiritConfPath, "system");
//		info("loadSystemConfigProperties: "+dir);
		this.loadPropertiesFromPath(dir);
	}
	
	private void loadPropertiesFromPath(File dir){
//		info("loadPropertiesFromPath: "+dir);
		if(null == dir){
			return;
		}
		String path = dir.getAbsolutePath();
		if(!dir.exists()){
			return;
		}
		if(!dir.isDirectory()){
			return;
		}
		if(dir.isHidden() || !dir.canRead()){
			return;
		}
		this.loadPropertiesFromDir(dir);
	}
	
	private void loadPropertiesFromDir(File dir){
		File[] files = dir.listFiles(new PropertiesFileFilter(false));
		for(File file:files){
			this.loadPropertiesFromFile(file);
		}
	}
	
	private static class PropertiesFileFilter implements FileFilter{
		private boolean inclodeDir;
		
		public PropertiesFileFilter(boolean inclodeDir) {
			this.inclodeDir = inclodeDir;
		}

		@Override
		public boolean accept(File file) {
			if(file.isHidden()){
				return false;
			}
			if(!file.canRead()){
				return false;
			}
			
			if(file.isDirectory()){
				if(this.inclodeDir){
					return true;
				}else{
					return false;
				}
			}else{
				if(file.getName().endsWith(".properties")||file.getName().endsWith(".xml")){
					return true;
				}else{
					return false;
				}
			}
		}
	}
	
	private void loadPropertiesFromFile(File file){
//		info("loadPropertiesFromFile: "+file);
		Properties p = new Properties();
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			if(file.getName().endsWith(XML_FILE_EXTENSION)){
				p.loadFromXML(in);
			}else{
				p.load(in);
			}
		} catch (FileNotFoundException e) {
			// do nothing
		} catch (InvalidPropertiesFormatException e) {
			// do nothing
		} catch (IOException e) {
			// do nothing
		} finally{
			try {
				in.close();
			} catch (IOException e) {
				// do nothing
			}
		}
//		info("===============p properties===============");
//		printProperties(p);
//		info("===============before merge===============");
//		printProperties();
		mergeProperties(file, p, this.properties);
//		info("===============after merge===============");
//		printProperties();
	}
	
	private static void mergeProperties(File file, Properties from, Properties to){
		for(Enumeration en=from.propertyNames(); en.hasMoreElements(); ){
			String key = (String) en.nextElement();
			
			if(StringUtils.isNullOrEmpty(key)){
				continue;
			}
			
			// key loading order : system.property -> personal property file
			Object value = System.getProperty(key);
			if(null == value){
				// get key from property file
				value = from.getProperty(key);
				if(null == value){
					// potentially a non-String value
					value = from.get(key);
				}
			}

			to.put(key, (null==value) ? null:value.toString());
		}
	}
	
	@Override
	public String getProperty(String name) {
		return this.properties.getProperty(name);
	}

	@Override
	public String getProperty(String name, String defaultVal) {
		String value = this.properties.getProperty(name, defaultVal);
		return (null==value) ? defaultVal : value;
	}

	@Override
	public Properties getProperties() {
		return this.properties;
	}

	@Override
	public void mergeProperties(Properties properties) {
		mergeProperties(null, properties, this.properties);
	}

	public void printProperties(){
		@SuppressWarnings("unchecked")
		List<String> keys = new ArrayList<String>((Set)this.properties.keySet());
		Collections.sort(keys);
		for (String key:keys) {
			Object value = this.properties.getProperty(key);
			System.out.println(key+"="+value);
		}
	}
	
	public void printProperties(Properties properties){
		@SuppressWarnings("unchecked")
		List<String> keys = new ArrayList<String>((Set)properties.keySet());
		Collections.sort(keys);
		for (String key:keys) {
			Object value = properties.getProperty(key);
			System.out.println(key+"="+value);
		}
	}
}
