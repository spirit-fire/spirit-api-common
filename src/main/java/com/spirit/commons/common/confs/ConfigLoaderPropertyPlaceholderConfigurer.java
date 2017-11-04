package com.spirit.commons.common.confs;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.util.StringValueResolver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 继承 PropertyPlaceholderConfigurer
 * @author guoxiong
 *
 */
public class ConfigLoaderPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{

	private ConfigLoader configLoader = null;
	
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException{
		super.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
		if(null == this.configLoader){
			this.configLoader = ConfigLoaderFactory.getConfigLoader();
		}
		try {
			Properties properties = this.mergeProperties();
			// convert thr merged properties
			convertProperties(properties);
			// 合并到 ConfigLoader 的 properties 中
			this.configLoader.mergeProperties(properties);
		} catch (IOException e) {
			throw new BeanInitializationException("Could not load properties", e);
		}
		
		// 增加对包内 project.properties 的加载
		Properties insideProperties = new Properties();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("project.properties"), "UTF-8"));
			insideProperties.load(br);
			if(null!=insideProperties && !insideProperties.isEmpty()){
				this.configLoader.mergeProperties(insideProperties);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
		}finally{
			try {
				br.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
			}
		}
		
		Properties mergedProperties = this.configLoader.getProperties();
		if(null==mergedProperties || mergedProperties.isEmpty()){
//			info("=============== properties is empty ===============");
//			info("ConfigLoaderPropertyPlaceholderConfigurer end\n\n\n");
			return;
		}else{
//			info("=============== properties begin ===============");
//			printProperties(mergedProperties);
//			info("=============== properties end ===============");
//			info("now process properties ...");
			// 这里就将 properties 应用在 beanFactory 中, 如果在这里占位符(${})中的内容已经被替换,
			// 则在下一个 PropertyPlaceholderConfigurer 中由于无法找到占位符而无法替换其属性. 
			super.processProperties(beanFactory, mergedProperties);
//			info("ConfigLoaderPropertyPlaceholderConfigurer end\n\n\n");
		}
	}

	/**
	 * @return configLoader
	 */
	public ConfigLoader getConfigLoader() {
		return configLoader;
	}

	/**
	 * @param configLoader 要设置的 configLoader
	 */
	public void setConfigLoader(ConfigLoader configLoader) {
		this.configLoader = configLoader;
	}
	
	public void printProperties(Properties properties){
		@SuppressWarnings("unchecked")
		List<String> keys = new ArrayList<String>((Set)properties.keySet());
		Collections.sort(keys);
		for (String key:keys) {
			Object value = properties.getProperty(key);
			info(key+"="+value);
		}
	}
	
	private void info(String msg){
		System.out.println(msg);
	}

}
