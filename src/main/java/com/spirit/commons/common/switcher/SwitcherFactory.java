package com.spirit.commons.common.switcher;

public class SwitcherFactory {

    private static Switcher switcher;
    static{
        init();
    }

    /**
     * do not constructor
     */
    private SwitcherFactory(){

    }

    private synchronized static void init(){
        if(switcher == null){
            switcher = new Switcher();
        }
    }

    public static Switcher getSwitcher(){
        if(switcher == null){
            init();
        }
        return switcher;
    }
}
