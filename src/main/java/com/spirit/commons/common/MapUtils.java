package com.spirit.commons.common;

import java.util.Map;

public class MapUtils {

    public static<T, V> boolean isNullOrEmpty(Map<T, V> map){
        return map == null || map.isEmpty();
    }
}
