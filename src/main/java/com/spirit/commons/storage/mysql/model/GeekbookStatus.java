package com.spirit.commons.storage.mysql.model;

public class GeekbookStatus {

    public static final String COLUMN_LABEL_AID = "aid";
    public static final String COLUMN_LABEL_UID = "uid";
    public static final String COLUMN_LABEL_ORIGINAL_URL = "aioriginal_urld";
    public static final String COLUMN_LABEL_PICS = "pics";
    public static final String COLUMN_LABEL_DATA = "data";
    public static final String COLUMN_LABEL_TOPIC = "topic";
    public static final String COLUMN_LABEL_CREATE_TIME = "create_time";

    private long aid;

    private long uid;

    private String original_url;

    private String pics;

    private String data;

    private String topic;

    private String create_time;

    public GeekbookStatus(){

    }

    public long getAid() {
        return aid;
    }

    public void setAid(long aid) {
        this.aid = aid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
