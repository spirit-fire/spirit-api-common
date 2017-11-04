package com.spirit.commons.storage.mysql.dao;

import com.spirit.commons.common.ApiLogger;
import com.spirit.commons.common.StringUtils;
import com.spirit.commons.storage.mysql.model.GeekbookStatus;
import com.spirit.commons.storage.mysql.model.GeekbookStatusQueryParam;
import com.spirit.commons.storage.mysql.util.DaoExecptionUtil;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * just a demo
 */
public class JdbcTemplateDao {

    private Map<String, TableContainers> tableMap;

    public JdbcTemplateDao(){

    }

    /**
     * sql: insert into $db$.$table$ (aid, uid, originalUrl, pics, data, topic, create_time) values (?, ?, ?, ?, ?, ?, ?);
     * @param tableType
     * @param sqlType
     * @param status
     * @return
     */
    public boolean update(String tableType, String sqlType, GeekbookStatus status){
        TableContainers tableContainers = tableMap.get(tableType);
        long id = StringUtils.getHashCode(status.getTopic());
        JdbcTemplate jdbcTemplate = tableContainers.getTemplate(id, true);
        String sql = tableContainers.buildSql(sqlType, id);
        Object[] param = new Object[]{ status.getAid(), status.getUid(), status.getOriginal_url(),
                status.getPics(), status.getData(), status.getTopic(), status.getCreate_time() };
        boolean result = false;
        try{
            result = jdbcTemplate.update(sql, param) > 0;
        } catch (DataAccessException e) {
            if(DaoExecptionUtil.isDuplicate(e)){
                ApiLogger.info(new StringBuilder(sql).append(" is duplicate").toString());
            }else{
                ApiLogger.error(new StringBuilder(sql).append(" DataAccessException err_msg: ").append(e.getMessage()).toString());
            }
        }

        return result;
    }


    /**
     * sql: select * from $db$.$table$ where topic = ? order by aid desc limit ?;
     * @param queryParam
     * @return
     */
    public List<GeekbookStatus> getStatus(String tableType, String sqlType, GeekbookStatusQueryParam queryParam){
        TableContainers tableContainers = tableMap.get(tableType);
        long id = StringUtils.getHashCode(queryParam.getTopic());
        JdbcTemplate jdbcTemplate = tableContainers.getTemplate(id, true);
        String sql = tableContainers.buildSql(sqlType, id);
        Object[] param = new Object[]{queryParam.getTopic(), queryParam.getCount()};
        List<GeekbookStatus> list = new ArrayList<GeekbookStatus>();
        jdbcTemplate.query(sql, param, new RowMapper<GeekbookStatus>(){

            @Override
            public GeekbookStatus mapRow(ResultSet rs, int rowNum) throws SQLException {
                GeekbookStatus status = new GeekbookStatus();
                status.setAid(rs.getLong(GeekbookStatus.COLUMN_LABEL_AID));
                status.setUid(rs.getLong(GeekbookStatus.COLUMN_LABEL_UID));
                status.setOriginal_url(rs.getString(GeekbookStatus.COLUMN_LABEL_ORIGINAL_URL));
                status.setPics(rs.getString(GeekbookStatus.COLUMN_LABEL_PICS));
                status.setData(rs.getString(GeekbookStatus.COLUMN_LABEL_DATA));
                status.setTopic(rs.getString(GeekbookStatus.COLUMN_LABEL_TOPIC));
                status.setCreate_time(rs.getString(GeekbookStatus.COLUMN_LABEL_CREATE_TIME));
                list.add(status);
                return status;
            }
        });

        return list;
    }
}
