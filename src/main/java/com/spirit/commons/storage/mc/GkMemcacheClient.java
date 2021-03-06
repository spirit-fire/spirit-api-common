package com.spirit.commons.storage.mc;

import com.spirit.commons.storage.mc.memcached.MemcachedClient;

/**
 * Created by lgx on 2017/5/14.
 */
public class GkMemcacheClient {

    /** memcache client */
    private MemcachedClient memcachedClient;

    /** server port */
    private String serverPort;

    /**
     * default constructor
     */
    public GkMemcacheClient(){

    }

    /**
     * default init
     */
    public void init(){
        if(null!=this.memcachedClient){
            this.memcachedClient = null;
        }
        this.memcachedClient = new MemcachedClient(this.serverPort);
        this.memcachedClient.setPrimitiveAsString(true);
    }

    public boolean add(String key, Object value){
        return this.memcachedClient.add(key, value);
    }

    public boolean set(String key, Object value){
        return this.memcachedClient.set(key, value);
    }

    public Object get(String key){
        return this.memcachedClient.get(key);
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }

    public String getServerPort(){
        return this.serverPort;
    }
}
