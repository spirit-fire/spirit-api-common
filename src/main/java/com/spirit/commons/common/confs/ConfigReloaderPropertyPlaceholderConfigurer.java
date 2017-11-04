package com.spirit.commons.common.confs;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionVisitor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.PropertyPlaceholderHelper;
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
public class ConfigReloaderPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements ApplicationContextAware {

	private ConfigLoader configLoader = null;

	private String beanName;

	private BeanFactory beanFactory;

	/**
	 * 整个 bean 的初始化期间都需要 applicationContext
	 */
	private ApplicationContext applicationContext;

	public void setBeanName(String beanName) {
		this.beanName = beanName;
		super.setBeanName(beanName);
	}

	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		super.setBeanFactory(beanFactory);
	}


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
			processProperties(beanFactory, mergedProperties);
//			info("ConfigLoaderPropertyPlaceholderConfigurer end\n\n\n");
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

		private final PropertyPlaceholderHelper helper;

		private final PropertyPlaceholderHelper.PlaceholderResolver resolver;

		public PlaceholderResolvingStringValueResolver(Properties props) {
			this.helper = new PropertyPlaceholderHelper(
					placeholderPrefix, placeholderSuffix, valueSeparator, ignoreUnresolvablePlaceholders);
			this.resolver = new ConfigReloaderPropertyPlaceholderConfigurer.PropertyPlaceholderConfigurerResolver(props);
		}

		@Override
		public String resolveStringValue(String strVal) throws BeansException {
			String value = this.helper.replacePlaceholders(strVal, this.resolver);
			return (value.equals(nullValue) ? null : value);
		}
	}

	/**
	 * 根据placeholderName(不含${})在Properties中解析出属性值
	 */
	private class PropertyPlaceholderConfigurerResolver implements PropertyPlaceholderHelper.PlaceholderResolver {

		private final Properties props;

		private PropertyPlaceholderConfigurerResolver(Properties props) {
			this.props = props;
		}

		public String resolvePlaceholder(String placeholderName) {
			return ConfigReloaderPropertyPlaceholderConfigurer.this.resolvePlaceholder(placeholderName, props, SYSTEM_PROPERTIES_MODE_OVERRIDE);
		}
	}

	/**
	 * Visit each bean definition in the given bean factory and attempt to replace ${...} property
	 * placeholders with values from the given properties.
	 */
	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
			throws BeansException {

		StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(props);
		doProcessProperties(beanFactoryToProcess, valueResolver);
	}

	protected void doProcessProperties(ConfigurableListableBeanFactory beanFactoryToProcess,
									   StringValueResolver valueResolver) {

		BeanDefinitionVisitor visitor = new BeanDefinitionVisitor(valueResolver);

		String[] beanNames = beanFactoryToProcess.getBeanDefinitionNames();
		for (String curName : beanNames) {
			// Check that we're not parsing our own bean definition,
			// to avoid failing on unresolvable placeholders in properties file locations.
			if (!(curName.equals(this.beanName) && beanFactoryToProcess.equals(this.beanFactory))) {
				BeanDefinition bd = beanFactoryToProcess.getBeanDefinition(curName);
				try {
					visitor.visitBeanDefinition(bd);
				}
				catch (Exception ex) {
					throw new BeanDefinitionStoreException(bd.getResourceDescription(), curName, ex.getMessage(), ex);
				}
			}
		}

		// New in Spring 2.5: resolve placeholders in alias target names and aliases as well.
		beanFactoryToProcess.resolveAliases(valueResolver);

		// New in Spring 3.0: resolve placeholders in embedded values such as annotation attributes.
		beanFactoryToProcess.addEmbeddedValueResolver(valueResolver);
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
