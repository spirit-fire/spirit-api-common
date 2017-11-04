package com.spirit.commons.thd.thrift.demo.service;

import com.spirit.commons.thd.thrift.demo.core.SharedService;
import com.spirit.commons.thd.thrift.demo.core.SharedServiceImpl;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

public class SharedServiceServer {

    TServerSocket tServerSocket;

    TBinaryProtocol.Factory proFactroy;

    TProcessor tProcessor;

    TServer server;

    TThreadPoolServer.Args args;

    public SharedServiceServer(){

    }

    public synchronized void init(){
        try {
            tServerSocket = new TServerSocket(7911);
            proFactroy = new TBinaryProtocol.Factory();
            tProcessor = new SharedService.Processor(new SharedServiceImpl());
            args = new TThreadPoolServer.Args(tServerSocket);
            args.processor(tProcessor);
            args.protocolFactory(proFactroy);
            server = new TThreadPoolServer(args);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public void startServer(){
        System.out.println("Start server on port 7911...");
        server.serve();
    }

    public void close(){
        System.out.println("Close server on port 7911...");
        tServerSocket.close();
        server.stop();
    }

    public static void main(String[] args){
        SharedServiceServer sharedServiceServer = new SharedServiceServer();
        sharedServiceServer.init();
        sharedServiceServer.startServer();
    }
}
