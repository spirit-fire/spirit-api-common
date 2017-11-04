package com.spirit.commons.common.confs;

import java.util.Properties;

/**
 * config load interface
 * @author guoxiong
 *
 */
public interface ConfigLoader {

	public static final  ConfigLoader NULL = new NullConfigLoader();
	
	public void load();

	public String getProperty(String name);
	
	public String getProperty(String name, String defaultVal);
	
	public Properties getProperties();
	
	/**
	 * 合并其他properties 到 主properties
	 * @param properties
	 */
	public void mergeProperties(Properties properties);
	
	public static final class NullConfigLoader implements ConfigLoader{

		@Override
		public String getProperty(String name) {
			return null;
		}

		@Override
		public String getProperty(String name, String defaultVal) {
			return null;
		}

		@Override
		public Properties getProperties() {
			return new Properties();
		}

		@Override
		public void mergeProperties(Properties properties) {
		}

		@Override
		public void load() {
		}
		
	}
}
