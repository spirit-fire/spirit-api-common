package com.spirit.commons.common;

import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.*;

public class ApacheHttpClient {

    private HttpClient httpClient;

    private int connectionTimeout;

    private int socketTimeout;

    private int maxConnectionPerHost;

    private MultiThreadedHttpConnectionManager connectionManager;

    private ExecutorService httpPool;

    private int minThread;

    private int maxThread;

    private int maxSize = 50;

    private static final String DEFAULT_CHARSET = "utf-8";

    /**
     * default constructor
     */
    public ApacheHttpClient(){
        this(50, 50, 1000, 1, 10, 50);
    }

    public ApacheHttpClient(int maxConnectionPerHost, int connectionTimeout, int socketTimeout, int minThread, int maxThread, int maxSize){
        this.connectionTimeout = connectionTimeout;
        this.maxConnectionPerHost = maxConnectionPerHost;
        this.socketTimeout = socketTimeout;
        this.minThread = minThread;
        this.maxThread = maxThread;
        this.maxSize = maxSize;
        connectionManager = new MultiThreadedHttpConnectionManager();
        HttpConnectionManagerParams httpConnectionParams = connectionManager.getParams();
        httpConnectionParams.setDefaultMaxConnectionsPerHost(maxConnectionPerHost);
        httpConnectionParams.setMaxTotalConnections(600); // 这个值要小于TOMCAT线程池是800
        httpConnectionParams.setDefaultMaxConnectionsPerHost(this.maxConnectionPerHost);
        httpConnectionParams.setConnectionTimeout(this.connectionTimeout);
        httpConnectionParams.setSoTimeout(this.socketTimeout);

        HttpClientParams httpClientParams = new HttpClientParams();
        // 忽略 cookies
        httpClientParams.setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        this.httpClient = new HttpClient(httpClientParams, connectionManager);

        this.httpPool = new ThreadPoolExecutor(minThread, maxThread, 60000, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(this.maxSize));
    }

    public String getAsyncResponse(String url, Map<String, String> headers, int timeout){
        String response = null;
        Future<String> future = httpPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return get(url, headers, DEFAULT_CHARSET);
            }
        });
        try {
            response = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // log
        } catch (ExecutionException e) {
            // log
        } catch (TimeoutException e) {
            // log
        }
        return response;
    }

    public String get(String url, Map<String, String> headers, String charset){
        if(StringUtils.isNullOrEmpty(url)){
            return null;
        }

        String response;
        HttpMethod getMethod = new GetMethod(url);
        HttpMethodParams params = new HttpClientParams();
        params.setContentCharset(charset);
        params.setHttpElementCharset(charset);
        params.setUriCharset(charset);
        getMethod.setParams(params);
        dealWithHeader(getMethod, headers);
        response = executeHttpMethod(getMethod);
        return response;
    }

    public String postAsyncResponse(String url, Map<String, String> headers, int timeout){
        String response = null;
        Future<String> future = httpPool.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return post(url, headers, DEFAULT_CHARSET);
            }
        });
        try {
            response = future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            // log
        } catch (ExecutionException e) {
            // log
        } catch (TimeoutException e) {
            // log
        }
        return response;
    }

    public String post(String url, Map<String, String> headers, String charset){
        if(StringUtils.isNullOrEmpty(url)){
            return null;
        }

        String response;
        HttpMethod postMethod = new PostMethod(url);
        HttpMethodParams params = new HttpClientParams();
        params.setContentCharset(charset);
        params.setHttpElementCharset(charset);
        params.setUriCharset(charset);
        postMethod.setParams(params);
        dealWithHeader(postMethod, headers);
        response = executeHttpMethod(postMethod);
        return response;
    }

    private void dealWithHeader(HttpMethod method, Map<String, String> headers){
        if(MapUtils.isNullOrEmpty(headers)){
            return;
        }

        for(Map.Entry<String, String> entry : headers.entrySet()){
            method.setRequestHeader(entry.getKey(), entry.getValue());
        }
    }

    private String executeHttpMethod(HttpMethod method){
        String response = null;
        try {
            int status = this.httpClient.executeMethod(method);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            getOutPutStream(method, out, DEFAULT_CHARSET);
            response = new String(out.toByteArray());
        } catch (IOException e) {
            // log
        }
        return response;
    }

    private void getOutPutStream(HttpMethod method, ByteArrayOutputStream out, String charset){
        try {
            InputStream inputStream = method.getResponseBodyAsStream();
            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = inputStream.read(bytes)) != -1){
                out.write(bytes, 0, len);
            }
        } catch (IOException e) {
            // log
        }
    }
}
