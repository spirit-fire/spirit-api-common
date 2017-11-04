package com.spirit.commons.storage.mysql.util;

import org.springframework.dao.DataAccessException;

public class DaoExecptionUtil {

    public static boolean isDuplicate(DataAccessException e){
        if(e == null){
            return false;
        }

        String msg = e.getMessage();
        return msg.startsWith("PreparedStatementCallback")
                && msg.contains("com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException: Duplicate entry");
    }
}
