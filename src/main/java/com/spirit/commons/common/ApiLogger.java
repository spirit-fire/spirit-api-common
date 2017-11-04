package com.spirit.commons.common;

import com.spirit.commons.common.trace.RequestContext;
import org.apache.log4j.Logger;

public class ApiLogger {

    private static Logger infolog = Logger.getLogger("info");
    private static Logger warnlog = Logger.getLogger("warn");
    private static Logger errorlog = Logger.getLogger("error");

    private ApiLogger(){

    }

    public static void info(Object msg){
        if(infolog.isInfoEnabled()){
            msg = trace(msg);
            infolog.info(msg);
        }
    }

    public static void warn(Object msg){
        msg = trace(msg);
        warnlog.warn(msg);
    }

    public static void warn(Object msg, Throwable e){
        msg = trace(msg);
        warnlog.warn(msg, e);
    }

    public static void error(Object msg){
        msg = trace(msg);
        errorlog.error(msg);
    }

    public static void error(Object msg, Throwable e){
        msg = trace(msg);
        errorlog.error(msg, e);
    }

    public static String trace(Object msg){
        StringBuffer sb;
        if(msg == null){
            sb = new StringBuffer("null");
        }else{
            sb = new StringBuffer(msg.toString());
        }
        sb.append(" ");
        sb.append(getRequestId());
        return sb.toString();
    }

    private static String getRequestId(){
        String rid = "";
        RequestContext requestContext = RequestContext.get();
        if(requestContext == null){
            return rid;
        }else{
            StringBuffer sb = new StringBuffer();
            sb.append("r=").append(requestContext.getId());
            rid = sb.toString();
        }
        return rid;
    }


    public static void main(String[] args){
        for(int index=0; index<100; index++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    RequestContext requestContext = new RequestContext();
                    requestContext.init();
                    System.out.println(trace("record msg test"));
                    System.out.println("---------------");
                    requestContext.clear();
                }
            }).start();
        }
    }
}
