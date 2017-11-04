package com.spirit.commons.qiniu.service;

import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.HttpRequest;
import com.spirit.commons.common.MD5Utils;
import com.spirit.commons.qiniu.constant.QiniuAuthTokenConstants;
import com.spirit.commons.qiniu.constant.QiniuDealTypeConstants;
import com.spirit.commons.qiniu.model.QiniuConfigModel;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.HashMap;

/**
 * Created by lgx on 2017/5/14.
 */
public class QiniuUploadService {

    /** qiniu auth cache map */
    private static HashMap<String, Auth> qiniuAuthCacheMap = new HashMap<String, Auth>();

    /**
     * just for pic url
     * @param info
     */
    public static String upload(String info, int type){
        String response = "";
        switch (type){
            case QiniuDealTypeConstants.QINIU_URL_TYPE:
                response = uploadPicture(info);
                break;
            case QiniuDealTypeConstants.QINIU_FILEPATH_TYPE:
                response = uploadFile(info);
                break;
            default:
                break;
        }
        return response;
    }

    /**
     * upload picture url
     * 1、get bytes from url
     * 2、upload bytes
     * @param info
     */
    private static String uploadPicture(String info){
        String response = "";
        String pid = MD5Utils.md5(info);
        QiniuConfigModel config = new QiniuConfigModel(pid);

        String zone = config.getZone();
        String accessKey = config.getAccessKey();
        String secretKey = config.getSeecretkey();
        String bucketName = config.getBucketName();
        String url = config.getUrl();

        Auth auth = null;
        Zone zoneCfg = QiniuAuthTokenConstants.qiniuZoneMap.get(zone.split("_", -1)[0]);
        Configuration cfg = new Configuration(zoneCfg);
        UploadManager uploadManager = new UploadManager(cfg);
        try{
            if(qiniuAuthCacheMap.containsKey(zone)){
                auth = qiniuAuthCacheMap.get(zone);
            }else{
                auth = Auth.create(accessKey, secretKey);
                synchronized (qiniuAuthCacheMap){
                    if(!qiniuAuthCacheMap.containsKey(zone)){
                        qiniuAuthCacheMap.put(zone, auth);
                    }
                }
            }

            String upToken = auth.uploadToken(bucketName, pid);
            // get bytes from target url
            byte[] bytes = HttpRequest.doGetBytes(info);
            if(null==bytes || 0==bytes.length){
                ApiLogger.error(String.format("QiniuUploadService uploadPicture picture download error! pic_url: %s, bytes length: %d", info, bytes.length));
                return response;
            }

//            Response response = uploadManager.put(bytes, pid, upToken);
            uploadManager.put(bytes, pid, upToken);

            BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
            response = String.format("contentBucket_%s_%d_%d", pid, img.getWidth(), img.getHeight());
            img = null;
        }catch(Exception e){
            ApiLogger.error(String.format("QiniuUploadService uploadPicture upload picture error! pic_url: %s, err_msg: %s", info, e.getMessage()));
            e.printStackTrace();
        }
        return response;
    }

    /**
     * upload local file
     * @param filePath
     */
    private static String uploadFile(String filePath){
        return null;
    }

    /**
     * upload bytes
     * @param bytes
     */
    public static void upload(byte[] bytes){

    }
}
