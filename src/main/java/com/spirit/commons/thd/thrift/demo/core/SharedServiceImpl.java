package com.spirit.commons.thd.thrift.demo.core;

import org.apache.thrift.TException;

public class SharedServiceImpl implements SharedService.Iface {

    @Override
    public SharedStruct getStruct(int key) throws TException {
        SharedStruct struct = new SharedStruct();
        struct.setKey(key);
        struct.setValue("val"+key);
        return struct;
    }
}
