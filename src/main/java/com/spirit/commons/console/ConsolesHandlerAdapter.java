package com.spirit.commons.console;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.ToolUtils;
import com.spirit.commons.common.switcher.Switcher;
import com.spirit.commons.common.switcher.SwitcherFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import java.util.List;

public class ConsolesHandlerAdapter extends IoHandlerAdapter {

    private Switcher switcher = SwitcherFactory.getSwitcher();

    @Override
    public void messageReceived(IoSession session, Object message){
        String command = message.toString();
        ApiLogger.info("[console] ConsolesHandlerAdapter receive message: " + command);
        // eg. switcher xxx.switcher on/off
        List<String> list = ToolUtils.explode(command.trim(), " ");
        if(list.size() != 3
                && !list.get(0).equals("switcher")
                && !list.get(2).equals("on")
                && !list.get(2).equals("off")){
            return;
        }
        boolean flag = parseFlag(list.get(2));
        switcher.registerSwitch(list.get(1), flag);
    }

    private boolean parseFlag(String info){
        return info.equals("on");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause)
            throws Exception {
        ApiLogger.error("[console] ConsolesHandlerAdapter error. ", cause);
    }
}
