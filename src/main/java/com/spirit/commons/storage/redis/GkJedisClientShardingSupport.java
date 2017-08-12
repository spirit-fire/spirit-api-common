package com.spirit.commons.storage.redis;

import com.spirit.commons.common.MD5Utils;

import java.util.HashMap;

/**
 * Created by lgx on 2017/5/20.
 */
public class GkJedisClientShardingSupport {

    /**
     * gkJedisCilent map, 0=>gkJedisCilent0, 1=>gkJedisCilent1...
     */
    private HashMap<Integer, GkJedisCilent> gkJedisCilentHashMap;

    public GkJedisClientShardingSupport(){

    }

    /**
     * parse jedisclient index from key
     * @param key
     * @return
     */
    private int getClientIndexFromKey(String key){
        String md5 = MD5Utils.md5(key);
        int index = 0;
        index = Integer.parseInt(md5.substring(md5.length()-2), 16) % this.gkJedisCilentHashMap.size();
        return index;
    }

    public void set(String key, String value){
        int index = this.getClientIndexFromKey(key);
        this.gkJedisCilentHashMap.get(index).set(key, value);
    }

    public String get(String key){
        int index = this.getClientIndexFromKey(key);
        return this.gkJedisCilentHashMap.get(index).get(key);
    }

    public void setGkJedisCilentHashMap(HashMap<Integer, GkJedisCilent> gkJedisCilentHashMap) {
        this.gkJedisCilentHashMap = gkJedisCilentHashMap;
    }
}
