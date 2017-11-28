package com.spirit.commons.thd.acmatch.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.util.CollectionUtils;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.ToolUtils;

/**
 * com.spirit.commons.thd.acmatch.service class
 *
 * @author guoxiong
 * @date 2017/11/22 18:02
 */
public class GkAcMatchService {

    /**
     * do not construct
     */
    private GkAcMatchService() {

    }

    private static AcMatchService acMatchService;

    private volatile static List<String> keywords = new ArrayList<String>();

    private static ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();

    /**
     * scheduled at fixed rate(10min)
     */
    private static void updateKeyWords() {
        ApiLogger.info("GkAcMatchService updateKeyWords...");
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getKeyWords();
            }
        }, 10, 60*10, TimeUnit.SECONDS);
    }

    private synchronized static void getKeyWords() {
        Properties properties = null;
        try {
            properties = new Properties();
            properties.load(new InputStreamReader(GkAcMatchService.class.getClassLoader().getResourceAsStream("gk_css_acmatch.properties"), "UTF-8"));
            System.out.println(properties);
        } catch (IOException e) {
            ApiLogger.error("GkAcMatchService getKeyWords error, err_msg: " + e.getMessage());
        }
        HashSet<String> tmpSet = new HashSet<String>();
        List<String> list = new ArrayList<String>();
        for(Object key : properties.keySet()){
            if(!tmpSet.contains(key)){
                tmpSet.add((String) key);
                list.add((String) key);
            }
            List<String> pattern = ToolUtils.explode(properties.getProperty((String)key), "\\|");
            pattern.forEach(new Consumer<String>() {
                @Override
                public void accept(String s) {
                    keywords.add(s);
                    concurrentHashMap.putIfAbsent(s, (String)key);
                }
            });
        }
        System.out.println(keywords);
        System.out.println(concurrentHashMap);
    }

    private synchronized static void updateAcmatch() {
        ApiLogger.info("GkAcMatchService updateAcmatch...");
        if(CollectionUtils.isEmpty(keywords)) {
            getKeyWords();
        }
        acMatchService = AcMatchInstance.getInstance();
        acMatchService.init(keywords);
    }

    private static void update() {
        updateKeyWords();
        updateAcmatch();
    }

    /**
     * match string
     * @param data
     * @return
     */
    public static String match(String data) {
        if(acMatchService == null || CollectionUtils.isEmpty(keywords)){
            update();
        }

        List<String> list = new ArrayList<String>();
        try{
            list = acMatchService.match(data);
        } catch(Exception e){
            ApiLogger.error("GkAcMatchService match err! err_msg: " + e.getMessage());
        }

        System.out.println("GkAcMatchService match list: " + list);
        Map<String, Integer> recordMap = new HashMap<String, Integer>();
        String result = "";
        Integer max = 0;
        for(String s:list) {
            String pat = concurrentHashMap.get(s);
            if(recordMap.containsKey(pat)) {
                recordMap.put(pat, recordMap.get(pat)+1);
            } else {
                recordMap.put(pat, 1);
            }

            if(recordMap.get(pat) > max) {
                max = recordMap.get(pat);
                result = pat;
            }
        }
        return result;
    }

    /**
     * inner class, used to init GkAcMatchService.acMatchService
     */
    private static class AcMatchInstance {

        private static AcMatchService instance = new AcMatchService();

        public static AcMatchService getInstance() {
            return instance;
        }
    }

    public static void main(String[] args) {
        GkAcMatchService.getKeyWords();
        String s = "<pre name=\"code\" class=\"cpp\">/* SDSLib, A C dynamic strings library";
        System.out.println(GkAcMatchService.match(s));
    }
}
