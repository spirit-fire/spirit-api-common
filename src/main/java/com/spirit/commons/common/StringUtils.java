package com.spirit.commons.common;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

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

    /**
     * get bytes
     * @param data
     * @return
     */
    public static byte[] getBytes(String data){
        byte[] bytes = null;
        try {
            bytes = data.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // do nothing
        }
        return bytes;
    }

    /**
     * parse bytes
     * @param bytes
     * @return
     */
    public static String parseBytes(byte[] bytes){
        String data = "";
        if(bytes == null || bytes.length < 1){
            return data;
        }
        try {
            data = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // do nothing
        }

        return data;
    }

    /**
     * get parent dir
     * @param path
     * @return
     */
    public static String getParentDir(String path){
        if(isNullOrEmpty(path)){
            return null;
        }

        int index = path.lastIndexOf('/');
        if(index < 0){
            return null;
        } else {
            return path.substring(0, index);
        }
    }

    /**
     * get hash code
     * @param data
     * @return
     */
    public static long getHashCode(String data){
        if(isNullOrEmpty(data)){
            return 0;
        }

        return Math.abs(data.hashCode());
    }
}
