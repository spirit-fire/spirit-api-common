package com.spirit.commons.common;

import java.security.MessageDigest;

/**
 * Created by lgx on 2017/5/14.
 */
public class MD5Utils {

    private static MessageDigest md5 = null;

    private static boolean md5EffectiveFalg = false;

    private MD5Utils(){

    }

    private synchronized static void initMd5() throws Exception {
        if(null==md5 && !md5EffectiveFalg){
            md5 = MessageDigest.getInstance("MD5");
            md5EffectiveFalg = true;
        }
    }

    /**
     * md5
     * @param info
     * @return
     */
    public static String md5(String info){
        String md5Value = "";
        try{
            if(null==md5 && !md5EffectiveFalg){
                initMd5();
            }
            byte[] bytes = md5.digest(info.getBytes("utf-8"));

            StringBuffer stringBuffer = new StringBuffer();
            for(byte b : bytes){
                int num = b&0xff;
                if(num<16){
                    stringBuffer.append(0);
                }
                stringBuffer.append(Integer.toHexString(num));
            }
            md5Value = stringBuffer.toString();
        }catch(Exception e){

        }
        return md5Value;
    }
}
