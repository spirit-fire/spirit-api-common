package com.spirit.engine.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by lgx on 2017/5/14.
 */
public class HttpRequest {

    // do not constructor
    private HttpRequest(){

    }

    /**
     * 发送GET请求
     * @param urlInput	输入url
     * @param param		输入参数,与url拼接
     * @return
     * @throws Exception
     */
    public static String doGet(String urlInput, String param) throws Exception{
        //拼接url
        URL url = new URL(urlInput + param);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(1000);
        BufferedReader in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
        String ret = "";
        String inputLine;
        while ((inputLine = in.readLine()) != null){
            ret += inputLine;
        }
        in.close();
        return ret;
    }

    /**
     * get bytes from url
     * @param urlInput input url
     * @return
     * @throws Exception
     */
    public static byte[] doGetBytes(String urlInput) throws Exception{
        // deal url
        URL url = new URL(urlInput);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(3000);

        byte[] bytes = new byte[1024];
        int len = 0;

        InputStream in = urlConnection.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        while((len = in.read(bytes)) != -1){
            out.write(bytes, 0, len);
        }
        out.flush();
        in.close();
        byte bs[] = out.toByteArray();

        return bs;
    }

    /**
     * 发送post请求
     * @param urlInput
     * @param params
     * @return
     * @throws Exception
     */
    public static String doPost(String urlInput, String params) throws Exception{
        String ret = "";
        String inputLine = "";
        BufferedReader in = null;
        URL url = new URL(urlInput);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setReadTimeout(3000);
        urlConnection.setDoOutput(true);// 使用 URL 连接进行输出
        //urlConnection.setDoInput(true);// 使用 URL 连接进行输入
        urlConnection.setUseCaches(false);// 忽略缓存
        urlConnection.setRequestMethod("POST");// 设置URL请求方法
        if(null!=params&& 0<params.length()){
            urlConnection.getOutputStream().write(params.getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        in = new BufferedReader( new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

        while ((inputLine = in.readLine()) != null){
            ret += inputLine;
        }
        in.close();
        return ret;

    }
}
