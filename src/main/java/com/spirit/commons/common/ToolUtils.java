package com.spirit.commons.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lgx on 2017/5/20.
 */
public class ToolUtils {

    /**
     * is string empty
     * @param input
     * @return
     */
    public static boolean isStringEmpty(String input){
        return (null==input || input.isEmpty());
    }

    /**
     * explode string with sep
     * @param info
     * @param sep
     * @return
     */
    public static List<String> explode(String info, String sep){
        List<String> list = new ArrayList<String>();
        if(isStringEmpty(info) || isStringEmpty(sep)){
            return list;
        }
        String[] array = info.split(sep,-1);
        for(String s:array){
            if(isStringEmpty(s)){
                continue;
            }
            list.add(s);
        }
        return list;
    }

    public static<T> String implode(List<T> list, String sep){
        String result = "";
        if(null==list || list.isEmpty() || isStringEmpty(sep)){
            return result;
        }
        for(int index=0; index<list.size(); index++){
            if(index==list.size()-1){
                result += list.get(index);
            }else{
                result += (list.get(index) + sep );
            }
        }
        return result;
    }
}
