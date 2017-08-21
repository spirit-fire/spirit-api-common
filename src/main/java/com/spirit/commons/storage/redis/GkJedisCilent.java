package com.spirit.commons.storage.redis;

import com.spirit.commons.common.ToolUtils;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Set;

/**
 * Created by lgx on 2017/5/14.
 */
public class GkJedisCilent {

    /** jediscluster */
    private JedisCluster jedisCluster;

    /** poolconfig */
    private JedisPoolConfig jedisPoolConfig;

    /** serverport, explode weith "," */
    private String serverPort;

    /** used to init jediscluster */
    private Set<HostAndPort> serverPortSet;

    /** default constructor */
    public GkJedisCilent(){

    }

    /**
     * parse hostandport from serverport, such as: 127.0.0.1:6379,127.0.0.1:6380....
     */
    private void parseServerPort(){
        if(null==this.serverPortSet || this.serverPortSet.isEmpty()){
            List<String> list = ToolUtils.explode(this.serverPort, ",");
            for(String hostport:list){
                List<String> tmp = ToolUtils.explode(hostport, ":");
                String host = tmp.get(0);
                int port = Integer.valueOf(tmp.get(1));
                HostAndPort hostAndPort = new HostAndPort(host, port);
                this.serverPortSet.add(hostAndPort);
            }
        }
    }

    /**
     * called just after construct
     */
    public void init(){
        this.parseServerPort();
        this.jedisCluster = new JedisCluster(this.serverPortSet, this.jedisPoolConfig);
    }

    /**
     * redis set ket value
     * @param key
     * @param value
     */
    public void set(String key, String value){
        this.jedisCluster.set(key, value);
    }

    /**
     * redis setex ket seconds value
     * @param key
     * @param value
     */
    public void setex(String key, int seconds, String value){
        this.jedisCluster.setex(key, seconds, value);
    }

    /**
     * redis get key
     * @param key
     * @return
     */
    public String get(String key){
        return this.jedisCluster.get(key);
    }

    /**
     * redis exists key
     * @param key
     * @return
     */
    public boolean exists(String key){
        return this.jedisCluster.exists(key);
    }

    /**
     * redis del key
     * @param key
     */
    public void del(String key){
        this.jedisCluster.del(key);
    }

    /**
     * redis del key1 key2...
     * @param keys
     */
    public void del(String... keys){
        this.jedisCluster.del(keys);
    }

    public void sadd(String key, String... value){
        this.jedisCluster.sadd(key, value);
    }

    public Long scard(String key){
        return this.jedisCluster.scard(key);
    }

    public boolean sismember(String key, String value){
        return  this.jedisCluster.sismember(key, value);
    }

    public Set<String> smembers(String key){
        return this.jedisCluster.smembers(key);
    }

    public void srem(String key, String value){
        this.jedisCluster.srem(key, value);
    }

    /**
     * redis mget key1,key2...
     * @param keys
     * @return
     */
    public List<String> mget(String... keys){
        return this.jedisCluster.mget(keys);
    }

    /**
     * redis mset key1,key2,value1,value2
     * @param keyvalues
     */
    public void mset(String... keyvalues){
        this.jedisCluster.mset(keyvalues);
    }

    public void hset(String key, String field, String value){
        this.jedisCluster.hset(key, field, value);
    }

    public String hget(String key, String field){
        return this.jedisCluster.hget(key, field);
    }

    /**
     * redis zadd key score value
     * @param key
     * @param score
     * @param value
     */
    public void zadd(String key, double score, String value){
        this.jedisCluster.zadd(key, score, value);
    }

    public Set<String> zrange(String key, long start, long end){
        return this.jedisCluster.zrange(key, start, end);
    }

    public Set<Tuple> zrangeWithScores(String key, long start, long end){
        return this.jedisCluster.zrangeWithScores(key, start, end);
    }

    public Set<String> zrangByScores(String key, double min, double max){
        return this.jedisCluster.zrangeByScore(key, min, max);
    }

    public Long zcard(String key){
        return this.jedisCluster.zcard(key);
    }

    public void zrem(String key, String... members){
        this.jedisCluster.zrem(key, members);
    }

    public void setJedisPoolConfig(JedisPoolConfig jedisPoolConfig) {
        this.jedisPoolConfig = jedisPoolConfig;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
    }
}
