package com.spirit.commons.console;

import com.spirit.commons.common.ApiLogger;
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

/**
 * mina 监听端口, 用于开关控制
 */
public class Consoles {

    private int port;

    private IoHandlerAdapter handler;

    private IoAcceptor acceptor;

    private static final int READ_BUFERSIZE = 4096;

    public Consoles(int port){
        this.port = port;
    }

    /**
     * init method
     */
    public void init(){
        handler = new ConsolesHandlerAdapter();
        acceptor = new NioSocketAcceptor();
        acceptor.getFilterChain().addLast("logger", new LoggingFilter());
        acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"))));
        acceptor.setHandler(handler);
        acceptor.getSessionConfig().setReadBufferSize(READ_BUFERSIZE);
        acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
        try {
            acceptor.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            ApiLogger.error("[console] Consoles bind port error!", e);
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
            ApiLogger.info("[console] Consoles destroy success!");
        }
    }

    public static void main(String[] args){
        Consoles consoles = new Consoles(9001);
        consoles.init();
    }
}
