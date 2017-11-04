package com.spirit.commons.common.confs;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationContextHolder {

    private static ConfigurableApplicationContext _context;

    private static Map<String, ConfigurableApplicationContext> _modules = new HashMap<String, ConfigurableApplicationContext>();

    public static void setApplicationContext(ConfigurableApplicationContext context){
        _context = context;
    }

    public static ConfigurableApplicationContext getApplicationContext() {
        return _context;
    }

    public static Map<String, ConfigurableApplicationContext> getModules() {
        return _modules;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name){
        return (T)_modules.get(name);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz){
        String[] names =_context.getBeanNamesForType(clazz);
        return (T)_modules.get(names[0]);
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> getBeans(Class<T> clazz){
        String[] names =_context.getBeanNamesForType(clazz);
        List<T> list = new ArrayList<T>();
        if(names == null || names.length < 1){
            return list;
        }

        for(String name : names){
            list.add((T)_modules.get(name));
        }
        return list;
    }

    public static void autowarebean(Object obj){
        if(_context != null){
            AutowireCapableBeanFactory factory = _context.getAutowireCapableBeanFactory();
            factory.autowireBean(obj);
        }
    }
}
