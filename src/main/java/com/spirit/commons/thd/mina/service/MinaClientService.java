package com.spirit.commons.thd.mina.service;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.thd.mina.dao.SocketHandler;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaClientService {

    private String ip;

    private int port;

    private IoConnector connector;

    private IoSession session;

    private IoHandlerAdapter handler;

    public MinaClientService(String ip, int port){
        this.ip = ip;
        this.port = port;
        init();
    }

    /**
     * init method
     */
    public void init(){
        handler = new SocketHandler();
        connector = new NioSocketConnector();
        connector.getFilterChain().addLast("logger", new LoggingFilter());
        connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(
                new TextLineCodecFactory(Charset.forName("UTF-8"))));
        connector.setHandler(handler);
        ConnectFuture connectFuture = connector.connect(new InetSocketAddress(ip, port));
        connectFuture.awaitUninterruptibly();
        session = connectFuture.getSession();
        ApiLogger.info(String.format(this.getClass().getName()+" connect to ip: %s, port: %d success!", ip, port));
    }

    /**
     * destroy method
     */
    public void destroy(){
        if(session != null && session.isConnected()){
            session.getCloseFuture().awaitUninterruptibly();
            ApiLogger.info("MinaClientService destroy session success!");
        }
        if(connector != null){
            connector.dispose(true);
            ApiLogger.info("MinaClientService destroy connector success!");
        }
    }

    /**
     * send message to server
     * @param message
     */
    public void sendMessage(String message){
        session.write(message);
    }

    public static void main(String[] args){
        MinaServerService server = new MinaServerService(9001, 1024);
        MinaClientService client = new MinaClientService("127.0.0.1", 9001);

        client.sendMessage("msg: send message 0");
        client.sendMessage("msg: send message 1");
        client.sendMessage("msg: send message 2");
        client.sendMessage("msg: send message 3");

        client.destroy();
        server.destroy();
        System.exit(0);
    }
}
