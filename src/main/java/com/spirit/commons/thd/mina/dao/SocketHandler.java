package com.spirit.commons.thd.mina.dao;

import com.spirit.commons.common.ApiLogger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class SocketHandler extends IoHandlerAdapter {

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {
        // log info, do nothing
        System.out.println(this.getClass().getName() + " messageReceived receive message: " + message.toString());
        ApiLogger.info(this.getClass().getName() + " messageReceived receive message: " + message.toString());
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        ApiLogger.error(this.getClass().getName() + " exceptionCaught error. ", cause);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        // log info, do nothing
        System.out.println(this.getClass().getName() + " messageReceived send message: " + message.toString());
        ApiLogger.info(this.getClass().getName() + " messageReceived send message: " + message.toString());
    }
}
