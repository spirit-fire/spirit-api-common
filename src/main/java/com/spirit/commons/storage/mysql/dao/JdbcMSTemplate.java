package com.spirit.commons.storage.mysql.dao;

import java.util.Random;

/**
 * m-s jdbc template
 */
public class JdbcMSTemplate {

    private JdbcTemplate master;

    private JdbcTemplate[] slave;

    private Random random = new Random();

    public JdbcMSTemplate(){

    }

    public JdbcTemplate getJdbcTemplate(boolean isWrite){
        if(isWrite){
            return master;
        } else {
            if(slave != null && slave.length > 0){
                int index = random.nextInt(slave.length);
                return slave[index];
            }
        }

        return  master;
    }

    public JdbcTemplate getMaster() {
        return master;
    }

    public void setMaster(JdbcTemplate master) {
        this.master = master;
    }

    public JdbcTemplate[] getSlave() {
        return slave;
    }

    public void setSlave(JdbcTemplate[] slave) {
        this.slave = slave;
    }
}
