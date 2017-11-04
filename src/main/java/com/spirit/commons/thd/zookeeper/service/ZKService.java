package com.spirit.commons.thd.zookeeper.service;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper service
 */
public class ZKService implements Watcher{

    /** connect */
    private String server;

    /** session timeout */
    private int timeout;

    /** zookeeper client */
    private ZooKeeper zookeeper;

    /** readOnly flag */
    private boolean readOnly;

    private static final int ZOOKEEPER_SESSION_TIMEOUT = 5000;

    public ZKService(String server){
        this(server, ZOOKEEPER_SESSION_TIMEOUT);
    }

    public ZKService(String server, int timeout){
        this(server, timeout, false);
    }

    public ZKService(String server, int timeout, boolean readOnly){
        this.server = server;
        this.timeout = timeout;
        this.readOnly = readOnly;
        init();
    }

    private void init(){
        try {
            zookeeper = new ZooKeeper(server, timeout, this, readOnly);
            ApiLogger.info(String.format("ZKService init success! server: %s, session timeout: %d, readOnly: %b.",
                    server, timeout, readOnly));
        } catch (IOException e) {
            ApiLogger.error(String.format("ZKService init failed! server: %s, session timeout: %d, readOnly: %b.",
                    server, timeout, readOnly), e);
        }
    }

    /**
     *
     * 监控所有出发的事件, 这里只记录log
     *
     * @param watchedEvent
     */
    @Override
    public void process(WatchedEvent watchedEvent) {
        ApiLogger.info("[watch] ZKService watch event " + watchedEvent.getType());
//        System.out.println("[watch] ZKService watch event " + watchedEvent.getType());
    }

    /**
     *
     * 节点是否存在
     *
     * @param path
     * @return
     */
    public Stat exists(String path){
        Stat stat = null;
        try {
            stat = zookeeper.exists(path, this);
        } catch (KeeperException e) {
            ApiLogger.error(String.format("ZKService exists KeeperException error! path:%s.", path), e);
        } catch (InterruptedException e) {
            ApiLogger.error(String.format("ZKService exists InterruptedException error! path:%s.", path), e);
        }
        return stat;
    }

    /**
     *
     * 创建 zookeeper 节点
     *
     * @param path
     * @param data
     * @param aclList
     * @param createMode
     * @return
     */
    public boolean createNode(String path, String data, List<ACL> aclList, CreateMode createMode){
        boolean flag = true;
        try {
            zookeeper.create(path, StringUtils.getBytes(data), aclList, createMode);
            ApiLogger.info(String.format("ZKService createNode path:%s, data:%s.", path, data));
        } catch (KeeperException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService createNode KeeperException error! path:%s, data:%s.", path, data), e);
        } catch (InterruptedException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService createNode InterruptedException error! path:%s, data:%s.", path, data), e);
        }

        return flag;
    }

    /**
     *
     * 递归的构建节点
     *
     * @param path
     * @param data
     * @param aclList
     * @param createMode
     */
    public void createNodes(String path, String data, List<ACL> aclList, CreateMode createMode){
        if(StringUtils.isNullOrEmpty(path)){
            return;
        }

        String parent = StringUtils.getParentDir(path);
        if(!StringUtils.isNullOrEmpty(parent) && !parent.equals("/")){
            Stat stat = exists(parent);
            if(stat == null){
                createNodes(parent, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
            }
        }
        createNode(parent, data, ZooDefs.Ids.OPEN_ACL_UNSAFE, createMode);
    }

    /**
     *
     * 删除节点
     *
     * @param path
     * @return
     */
    public boolean deleteNode(String path){
        boolean flag = true;
        try {
            zookeeper.delete(path, -1);
        } catch (InterruptedException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService deleteNode InterruptedException error! path:%s.", path), e);
        } catch (KeeperException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService deleteNode KeeperException error! path:%s.", path), e);
        }
        return flag;
    }

    /**
     *
     * 递归的删除节点及其所有子节点
     *
     * @param path
     * @return
     */
    public void deleteNodes(String path){
        List<String> list = getChildren(path);
        for(String node : list){
            deleteNodes(path+"/"+node);
        }
        deleteNode(path);
    }

    /**
     *
     * 获取 zookeeper path 下所有子节点列表
     *
     * @param path
     * @return
     */
    public List<String> getChildren(String path){
        List<String> list = new ArrayList<String>();
        try {
            list = zookeeper.getChildren(path, this);
        } catch (KeeperException e) {
            ApiLogger.error(String.format("ZKService getChildren KeeperException error! path:%s.", path), e);
        } catch (InterruptedException e) {
            ApiLogger.error(String.format("ZKService getChildren InterruptedException error! path:%s.", path), e);
        }

        return list;
    }

    /**
     *
     * 修改节点数据
     *
     * @param path
     * @param data
     * @return
     */
    public boolean setData(String path, String data){
        boolean flag = true;
        try {
            zookeeper.setData(path, StringUtils.getBytes(data), -1);
        } catch (KeeperException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService setData KeeperException error! path:%s, data:%s.", path, data), e);
        } catch (InterruptedException e) {
            flag = false;
            ApiLogger.error(String.format("ZKService setData InterruptedException error! path:%s, data:%s.", path, data), e);
        }

        return flag;
    }

    /**
     *
     * 获取节点数据
     *
     * @param path
     * @return
     */
    public String getData(String path){
        String data = "";
        byte[] bytes = null;
        try {
            bytes = zookeeper.getData(path, true, null);
        } catch (KeeperException e) {
            ApiLogger.error(String.format("ZKService getData KeeperException error! path:%s, data:%s.", path, data), e);
        } catch (InterruptedException e) {
            ApiLogger.error(String.format("ZKService getData InterruptedException error! path:%s, data:%s.", path, data), e);
        }
        data = StringUtils.parseBytes(bytes);
        return data;
    }

    /**
     * close
     */
    public void close(){
        synchronized (this){
            try {
                zookeeper.close();
            } catch (InterruptedException e) {
                ApiLogger.error(String.format("ZKService close InterruptedException error!"), e);
            }
        }
    }

    public static void main(String[] args){
        ZKService zkService = new ZKService("127.0.0.1:2181");
        List<String> list = zkService.getChildren("/");
        System.out.println("get children list: " + list);
        boolean flag = zkService.createNode("/test1", "true", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create node /test1: " + flag);
        list = zkService.getChildren("/");
        System.out.println("get / children list: " + list);
        flag = zkService.createNode("/test2", "true", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create node /test2: " + flag);
        list = zkService.getChildren("/");
        System.out.println("get / children list: " + list);
        flag = zkService.createNode("/test1/tt", "true", ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        System.out.println("create node /test1/tt: " + flag);
        list = zkService.getChildren("/test1");
        System.out.println("get /test1 children list: " + list);

        zkService.deleteNodes("/test1");
        zkService.deleteNodes("/test2");
        list = zkService.getChildren("/");
        System.out.println("get / children list: " + list);

        zkService.close();
    }
}
