package com.spirit.commons.storage.mc;

import com.spirit.commons.common.StringUtils;
import com.spirit.commons.common.ToolUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by lgx on 2017/6/11.
 */
public class GkMemcacheClientShardingSupport {

    /** server ports, split by ',' */
    private String serverPorts = "";

    /** server port list, used to init GkMemcacheClient */
    private List<String> serverPortList = null;

    /** server port size */
    private int size = 0;

    /** GkMemcacheClient map */
    private Map<Integer, GkMemcacheClient> gkClientMap = null;

    /** random */
    private Random random = new Random();

    /** readable flag, if readable==true, start reading */
    private boolean readable = false;

    /** read key */
    private String readKey;

    /** write key */
    private String writeKey;

    /** blocking queue used to transfer data */
    private LinkedBlockingQueue<String> linkedBlockingQueue;

    /** blocking queue size */
    private int blockSize = 1000;

    /** default constructor */
    public GkMemcacheClientShardingSupport(){

    }

    /**
     * init method
     */
    private void init(){
        if(StringUtils.isNullOrEmpty(this.serverPorts)){
            return;
        }

        this.serverPortList = ToolUtils.explode(this.serverPorts, ",");
        this.size = this.serverPortList.size();
        for(int index =0; index<this.size; index++){
            GkMemcacheClient gkMemcacheClient = new GkMemcacheClient();
            gkMemcacheClient.setServerPort(this.serverPortList.get(index));
            gkMemcacheClient.init();
            this.gkClientMap.put(index, gkMemcacheClient);

            // start reading
            if(readable){
                this.startReading(gkMemcacheClient, this.readKey);
            }
        }

    }

    private void startReading(final GkMemcacheClient gkMemcacheClient, final String readKey){
        final LinkedBlockingQueue<String> blockingQueue = this.linkedBlockingQueue;
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String data = (String)gkMemcacheClient.get(readKey);
                    try {
                        blockingQueue.put(data);
                    } catch (InterruptedException e) {
                        // do nothing, just continue
                    }
                }
            }
        });
        t.start();
    }

    public int getIndexFromObjectByHashCode(Object object){
        return (null==object) ? 0 : Math.abs(object.hashCode())%this.size;
    }

    public int getIndexFromKeyAndCtimeByHashCode(Object key){
        StringBuilder sb = new StringBuilder();
        sb.append(key).append(System.currentTimeMillis());
        String str = sb.toString();
        return Math.abs(str.hashCode()) % this.size;
    }

    /**
     * loop writer
     * @param key
     * @param value
     * @return
     */
    public boolean add(String key, Object value){
        int index = this.getRandomIndex();
        boolean flag = false;
        for(int i = 0; i<this.size; i++){
            flag = this.gkClientMap.get((index + i) % this.size).add(key, value);
            if(flag){
                break;
            }
        }

        return flag;
    }

    public String get(){
        String data = null;
        try {
            data = this.linkedBlockingQueue.take();
        } catch (InterruptedException e) {
            // do nothing
        }

        return data;
    }

    /**
     * loop reader
     * @param key
     * @return
     */
    public String loopReader(String key){
        int index = this.getRandomIndex();
        Object data = null;
        for(int i = 0; i < this.size; i++){
            data = this.gkClientMap.get((index + i) % this.size).get(key);
            if(data != null){
                break;
            }
        }

        return data == null ? "" : (String) data;
    }

    /**
     * loop writer
     * @param key
     * @return
     */
    public boolean loopWriter(String key, Object value){
        int index = this.getRandomIndex();
        boolean flag = false;
        for(int i = 0; i < this.size; i++){
            flag = this.gkClientMap.get((index + i) % this.size).add(key, value);
            if(flag){
                break;
            }
        }

        return flag;
    }

    public boolean set(String key, Object value){
        int index = this.getIndexFromObjectByHashCode(value);
        return this.gkClientMap.get(index).set(key, value);
    }

    public Object get(String key){
        int index = this.getIndexFromKeyAndCtimeByHashCode(key);
        return this.gkClientMap.get(index).get(key);
    }

    private int getRandomIndex(){
        return random.nextInt(1024);
    }
}
