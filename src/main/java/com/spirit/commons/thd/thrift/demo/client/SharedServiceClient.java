package com.spirit.commons.thd.thrift.demo.client;

import com.spirit.commons.thd.thrift.demo.core.SharedService;
import com.spirit.commons.thd.thrift.demo.core.SharedStruct;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class SharedServiceClient {

    TTransport transport;

    TProtocol tProtocol;

    SharedService.Client client;

    public SharedServiceClient(){

    }

    public synchronized void init(){
        try {
            transport = new TSocket("localhost", 7911);
            transport.open();
            tProtocol = new TBinaryProtocol(transport);
            client = new SharedService.Client(tProtocol);
        } catch (TTransportException e) {
            e.printStackTrace();
        }
    }

    public void startClient(int key){
        try {
            SharedStruct struct = client.getStruct(key);
            System.out.println("key: "+struct.key+", value: "+struct.value);
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        System.out.println("Close client...");
        transport.close();
    }

    public static void main(String[] args){

    }
}
