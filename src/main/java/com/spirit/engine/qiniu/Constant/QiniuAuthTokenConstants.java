package com.spirit.engine.qiniu.Constant;

import com.qiniu.common.Zone;

import java.util.HashMap;

/**
 * Created by lgx on 2017/5/14.
 */
public class QiniuAuthTokenConstants {

    public static HashMap<String, Zone> qiniuZoneMap = new HashMap<String, Zone>();

    public static HashMap<Integer , String> qiniuZoneNameMap = new HashMap<Integer , String>();

    public static HashMap<String , HashMap<String, String>> qiniuAuthTokenMap = new HashMap<String , HashMap<String, String>>();

    public static HashMap<String , HashMap<String, String>> qiniuBucketNameMap = new HashMap<String , HashMap<String, String>>();

    static{
        // init qiniu zone map
        {
            qiniuZoneMap.put("zone0", Zone.zone0());
            qiniuZoneMap.put("zone1", Zone.zone1());
            qiniuZoneMap.put("zone2", Zone.zone2());
        }

        // init qiniu zone name map
        {
            qiniuZoneNameMap.put(0, "zone1_0");
        }

        // init qiniu auth token in each zone
        {
            qiniuAuthTokenMap.put("zone1_0", new HashMap<String, String>());
            qiniuAuthTokenMap.get("zone1_0").put("accessKey", "t6NrB_-eQYWJOwgO6_g8f2eAIlDBhgGI6Pw-hau6");
            qiniuAuthTokenMap.get("zone1_0").put("secretKey", "nQA_zI-H7IQYHiE_AhLWGuMWYNrmvSx0KK6ifH1V");
            qiniuAuthTokenMap.get("zone1_0").put("url", "http://ohsxibjkn.bkt.clouddn.com/");
        }

        // init bucketname in each zone
        {
            qiniuBucketNameMap.put("zone1_0", new HashMap<String, String>());
            qiniuBucketNameMap.get("zone1_0").put("bucketname0", "for-ttx");;
        }
    }

}
