package com.spirit.commons.storage.mysql.dao;

import com.spirit.commons.storage.mysql.util.TableUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * m-s mysql containers
 */
public class TableContainers {

    private JdbcMSTemplate[] jdbcMSTemplates;

    private Map<String, String> sqls;

    private int tableNum;

    private String tableNamePrefix;

    private String dbName;

    private Map<String, JdbcTemplate> jdbcTemplateMap = new ConcurrentHashMap<String, JdbcTemplate>();

    public TableContainers(){

    }

    public JdbcTemplate getTemplate(long id, boolean isWrite){
        int db = TableUtils.getDbIndex(id, jdbcMSTemplates.length);
        return jdbcMSTemplates[db].getJdbcTemplate(isWrite);
    }

    public String getSql(String info){
        return sqls.get(info);
    }

    public String buildSql(String sqlType, long id){
        String sql = getSql(sqlType);
        String tableName = TableUtils.buildTableName(tableNamePrefix, id, tableNum);
        sql = TableUtils.buildSql(dbName, tableName, sql);
        return sql;
    }

    public JdbcMSTemplate[] getJdbcMSTemplates() {
        return jdbcMSTemplates;
    }

    public void setJdbcMSTemplates(JdbcMSTemplate[] jdbcMSTemplates) {
        this.jdbcMSTemplates = jdbcMSTemplates;
    }

    public Map<String, String> getSqls() {
        return sqls;
    }

    public void setSqls(Map<String, String> sqls) {
        this.sqls = sqls;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public String getTableNamePrefix() {
        return tableNamePrefix;
    }

    public void setTableNamePrefix(String tableNamePrefix) {
        this.tableNamePrefix = tableNamePrefix;
    }

    public Map<String, JdbcTemplate> getJdbcTemplateMap() {
        return jdbcTemplateMap;
    }

    public void setJdbcTemplateMap(Map<String, JdbcTemplate> jdbcTemplateMap) {
        this.jdbcTemplateMap = jdbcTemplateMap;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}
