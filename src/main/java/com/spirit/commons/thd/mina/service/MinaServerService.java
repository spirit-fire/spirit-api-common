package com.spirit.commons.thd.mina.service;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.thd.mina.dao.SocketHandler;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaServerService {

    private int port;

    private int bufSize;

    private IoHandlerAdapter handler;

    private IoAcceptor acceptor;

    public MinaServerService(){

    }

    public MinaServerService(int port, int bufSize){
        this.port = port;
        this.bufSize = bufSize;
        init();
    }

    /**
     * init method
     */
    public void init(){
        handler = new SocketHandler();
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"))));
        acceptor.setHandler(handler);
        acceptor.getSessionConfig().setReadBufferSize(bufSize);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            ApiLogger.error("MinaServerService bind port error!", e);
            System.exit(0);
        }
    }

    /**
     * destroy method
     */
    public void destroy(){
        if(acceptor != null){
            acceptor.unbind();
            acceptor.dispose(true);
            ApiLogger.info("MinaServerService destroy success!");
        }
    }
}
