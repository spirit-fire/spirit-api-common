package com.spirit.commons.common.switcher;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.MapUtils;

import java.util.HashMap;
import java.util.Map;

public class Switcher {

    /** switch map */
    private Map<String, Boolean> switchMap;

    public Switcher(){
        init();
    }

    private void init(){
        switchMap = new HashMap<String, Boolean>();
    }

    /**
     * get switch
     * @param key
     * @return
     */
    public boolean getSwitch(String key){
        boolean flag = false;
        return switchMap.getOrDefault(key, false);
    }

    /**
     * register switch
     * @param key
     * @param flag
     */
    public void registerSwitch(String key, Boolean flag){
        switchMap.put(key, flag);
        ApiLogger.info(String.format("[Switcher] registerSwitch key: %s, flag: %b", key, flag));
    }

    /**
     * return switch is true
     * @param key
     * @return
     */
    public boolean isOpen(String key){
        return getSwitch(key);
    }

    /**
     * return switch is false
     * @param key
     * @return
     */
    public boolean isClosed(String key){
        return !getSwitch(key);
    }

    /**
     * get all switcher in switchMap
     * @return
     */
    public String getAllSwitcher(){
        StringBuffer sb = new StringBuffer();
        if(!MapUtils.isNullOrEmpty(switchMap)){
            for(Map.Entry<String, Boolean> entry : switchMap.entrySet()){
                sb.append(entry.getKey()).append("=")
                        .append(entry.getValue()).append("\n");
            }
        }
        return sb.toString();
    }
}
