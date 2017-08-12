package com.spirit.commons.qiniu.model;

import com.spirit.commons.qiniu.constant.QiniuAuthTokenConstants;

/**
 * Created by lgx on 2017/5/14.
 */
public class QiniuConfigModel {

    private String zone;

    private String seecretkey;

    private String accessKey;

    private String url;

    private String bucketName;

    public QiniuConfigModel(){

    }

    public QiniuConfigModel(String pid){
        String sub = pid.substring(pid.length()-2);
        int index = Integer.parseInt(sub, 16);
        int zoneNum = index % QiniuAuthTokenConstants.qiniuZoneNameMap.size();
        int bucketNum = index % QiniuAuthTokenConstants.qiniuBucketNameMap.get(QiniuAuthTokenConstants.qiniuZoneNameMap.get(zoneNum)).size();
        this.zone = QiniuAuthTokenConstants.qiniuZoneNameMap.get(zoneNum);
        this.accessKey = QiniuAuthTokenConstants.qiniuAuthTokenMap.get(this.zone).get("accessKey");
        this.seecretkey = QiniuAuthTokenConstants.qiniuAuthTokenMap.get(this.zone).get("secretKey");
        this.url = QiniuAuthTokenConstants.qiniuAuthTokenMap.get(this.zone).get("url");
        this.bucketName = QiniuAuthTokenConstants.qiniuBucketNameMap.get(this.zone).get("bucketname"+bucketNum);
    }

    public String getZone(){return this.zone;}
    public String getAccessKey(){return this.accessKey;}
    public String getSeecretkey(){return this.seecretkey;}
    public String getBucketName(){return this.bucketName;}
    public String getUrl(){return this.url;}
}
