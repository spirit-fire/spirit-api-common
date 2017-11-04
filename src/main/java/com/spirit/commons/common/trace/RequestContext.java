package com.spirit.commons.common.trace;

import com.spirit.commons.common.IpUtils;

import java.util.Calendar;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class RequestContext {

    private static final ThreadLocal<RequestContext> REQUEST_CONTEXT_THREAD_LOCAL = new ThreadLocal<RequestContext>();

    private static final AtomicLong ATOMIC_LONG = new AtomicLong();

    private static long refreshId;

    private long id;

    static{
        // refreshId = ((intIp << 6) | minute ) << 32
        int intIp = IpUtils.ipToInteger(IpUtils.getIpHost()) & 0x00FFFFFF;
        int minute = Calendar.getInstance().get(Calendar.MINUTE);
        refreshId = ((long)((intIp << 6) | minute)) << 32 ;
    }

    public static void init(){
        REQUEST_CONTEXT_THREAD_LOCAL.set(new RequestContext(refreshId | (ATOMIC_LONG.incrementAndGet() & 0x7FFFFFFF)));
    }

    public RequestContext(){

    }

    public RequestContext(long id){
        this.id = id;
    }

    public static RequestContext get(){
        return REQUEST_CONTEXT_THREAD_LOCAL.get();
    }

    public long getId(){
        return id;
    }

    public static void clear(){
        REQUEST_CONTEXT_THREAD_LOCAL.remove();
    }

    public static void finish(){
        REQUEST_CONTEXT_THREAD_LOCAL.remove();
    }

    public static void main(String[] args){
        RequestContext ctx = RequestContext.get();
        for(int i=0; i<10; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(ctx.getId());
                }
            }).start();

        }
    }
}
