package com.spirit.commons.storage.mysql.util;

import com.spirit.commons.common.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TableUtils {

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");;

    public static int getDbIndex(long id, int dbNum){
        return (int) (id % dbNum);
    }

    public static int getTableIndex(long id, int tableNum){
        return (int) (id % tableNum);
    }

    public static String buildTableName(String tableNamePrefix, long id, int tableNum){
        int index = (int) (id % tableNum);
        return new StringBuilder(tableNamePrefix).append("_").append(index).toString();
    }

    public static String buildSql(String dbName, String tableName, String sql){
        if(StringUtils.isNullOrEmpty(dbName)
                || StringUtils.isNullOrEmpty(tableName)
                || StringUtils.isNullOrEmpty(sql)){
            return sql;
        }
        sql = sql.replace("$db$", dbName);
        sql = sql.replace("$table$", tableName);
        return sql;
    }

    public static String getCreateTime(long ctime){
        return simpleDateFormat.format(ctime);
    }
}
