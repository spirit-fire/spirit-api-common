package com.spirit.commons.common.confs;

/**
 * 
 * @author guoxiong
 *
 */
public class ConfigLoaderFactory {

	static class InstanceHolder{
		static PropertiesConfigLoader loader = new PropertiesConfigLoader();
		static{
			loader.load();
		}
	}
	
	public static ConfigLoader getConfigLoader(){
		return getConfigLoader(true);
	}
	
	public static ConfigLoader getConfigLoader(boolean throwException){
		try {
			return InstanceHolder.loader;
		} catch (Exception e) {
			// TODO: handle exception
			if(throwException){
				throw e;
			}else{
				return null;
			}
		}
	}
}
