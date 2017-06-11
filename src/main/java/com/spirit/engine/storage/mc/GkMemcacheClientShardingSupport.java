package com.spirit.engine.storage.mc;

import com.spirit.engine.common.StringUtils;
import com.spirit.engine.common.ToolUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;

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

    /** random get client */
    private Random random = new Random();

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
        }
    }

    private int getRandom(){
        return this.random.nextInt(this.size);
    }

    public boolean add(String key, Object value){
        int index = this.getRandom();
        return this.gkClientMap.get(index).add(key, value);
    }

    public Object get(String key){
        int index = this.getRandom();
        return this.gkClientMap.get(index).get(key);
    }
}
