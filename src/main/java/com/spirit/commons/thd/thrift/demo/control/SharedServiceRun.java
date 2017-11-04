package com.spirit.commons.thd.thrift.demo.control;

import com.spirit.commons.thd.thrift.demo.client.SharedServiceClient;
import com.spirit.commons.thd.thrift.demo.service.SharedServiceServer;

public class SharedServiceRun {

    public static void main(String[] args){
        SharedServiceServer sharedServiceServer = new SharedServiceServer();
        sharedServiceServer.init();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                sharedServiceServer.startServer();
            }
        });
        thread.start();

        SharedServiceClient sharedServiceClient = new SharedServiceClient();
        sharedServiceClient.init();
        for(int i=0; i<100; i++){
            sharedServiceClient.startClient(100000 + i);
        }

        sharedServiceClient.close();
        sharedServiceServer.close();
        thread.interrupt();
    }
}
