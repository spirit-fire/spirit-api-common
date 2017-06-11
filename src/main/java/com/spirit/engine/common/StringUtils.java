package com.spirit.engine.common;

/**
 * Created by lgx on 2017/6/11.
 */
public class StringUtils {

    /**
     * private constructor, do not init this class
     */
    private StringUtils(){

    }

    /**
     * decision str is null or empty
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str){
        return null==str || str.isEmpty();
    }
}
