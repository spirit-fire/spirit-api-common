package com.spirit.commons.common;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

public class IpUtils {

    private IpUtils(){

    }

    /**
     * 如果 getIpHostAddress 取出的 ip 为 127.0.0.1, 需要重新取一次 host
     * @return
     * @throws SocketException
     */
    public static String getIpHost() {
        String ipHost = "127.0.0.1";
        Enumeration<NetworkInterface> enumeration = null;
        try {
            enumeration = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration
                        .nextElement();
                Enumeration<InetAddress> e = networkInterface.getInetAddresses();
                while(e.hasMoreElements()){
                    ip = (InetAddress) e.nextElement();
                    if(null!=ip && ip instanceof Inet4Address && !ip.getHostAddress().equals("127.0.0.1")){
                        ipHost = ip.getHostAddress();
//                    // 这里只要10.开头的呀
//                    if(!ipHost.substring(0,3).equals("10.")){
//                        continue;
//                    }
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            // log
        }

        return ipHost;
    }

    /**
     * full ip to int
     * @param ipAddress
     * @return
     */
    public static int ipToInteger(final String ipAddress){
        int ip = 0;
        List<String> list = ToolUtils.explode(ipAddress, "\\.");
        for(int index=0; index < list.size(); index++){
            ip <<= 8;
            ip |= Integer.parseInt(list.get(index));
        }
        return ip;
    }
}
