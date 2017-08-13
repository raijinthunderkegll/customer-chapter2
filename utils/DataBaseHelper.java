package com.chy.chapter2.utils;


import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.BeanMapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 数据库操作工具类
 * Created by yang on 2017/7/18.
 */
public class DataBaseHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_HOLDER;

    private static final QueryRunner QUERY_RUNNER;

    private static final BasicDataSource DATA_SOURCE;

    static {

        Properties properties = PropsUtil.loadProps("jdbc.properties");
        String driver = properties.getProperty("jdbc.driver");
        String url = properties.getProperty("jdbc.url");
        String username = properties.getProperty("jdbc.username");
        String password = properties.getProperty("jdbc.password");

        CONNECTION_HOLDER = new ThreadLocal<>();
        QUERY_RUNNER = new QueryRunner();
        DATA_SOURCE = new BasicDataSource();

        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);
    }

    /**
     * 获取connection连接
     */
    public static Connection getConnection() {
        Connection conn = CONNECTION_HOLDER.get();
    if(conn == null){
        try{
            conn = DATA_SOURCE.getConnection();
        }catch (SQLException e){
            LOGGER.error("get connection failure",e);
            throw new RuntimeException(e);
        }finally {
            CONNECTION_HOLDER.set(conn);
        }

    }
        return conn;
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.error("close connection failure", e);
            }
        }
    }

    /**
     * 将查询结果的Map List 返回成具体类型的对象集合
     */
    public static <T> List<T> changeMapListToEntityList(Class<T> entityClass, List<Map<String, Object>> mapList) {
        if (CollectionUtil.isEmpty(mapList)) {
            LOGGER.error("change map list to entity list failure : mapList is empty");
            return null;
        }
        try {
            String keySet = null;
            T entity = null;
            List<T> list = new ArrayList<T>();
            for (Map<String, Object> map : mapList) {
                entity = entityClass.newInstance();
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    keySet = StringUtil.captureString(entry.getKey());
                    PropertyDescriptor pd = new PropertyDescriptor(entry.getKey(),entity.getClass());
                    Method setM = pd.getWriteMethod();
                    if(setM != null){
                        setM.invoke(entity,entry.getValue());
                    }
                }
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            LOGGER.error("reflact method error in changeMapListToEntityList", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 查询实体列表
     */
    public static List<Map<String, Object>> queryList(Connection conn, String sql, Object... params) {
        List<Map<String, Object>> entityList = null;

        try {
            entityList = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("query entity list failure", e);
        } finally {
            closeConnection(conn);
        }
        return entityList;
    }

    /**
     * 查询单个实体对象
     */
    public static <T> T queryEntity(Class<T> entityClass, Connection conn, String sql, Object... params) {
        T entity = null;

        try {
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass));
        } catch (SQLException e) {
            LOGGER.error("query entity faulure", e);
        }
        return entity;
    }

    /**
     * 执行sql  包含insert,update,delete
     */
    public static int excuteUpdate(Connection conn, String sql, Object... params) {
        int rows = 0;

        try {
            rows = QUERY_RUNNER.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error("excute update failure", e);
        }

        return rows;
    }

    /**
     * 根据实体类名获取表名
     */
    public static String getTableName(Class entityClass) {
        //TODO
        return entityClass.getSimpleName();
    }

    /**
     * 新增数据
     */
    public static <T> boolean insertEntity(Class<T> entityClass, Connection conn, Map<String, Object> fieldMap) {
        //fieldMap 的map集合中,key对应列名 , value对应行数据
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can't insert entity : fieldMap is empty");
            return false;
        }

        String sql = "INSERT INTO " + getTableName(entityClass);

        //拼装colums和values
        StringBuffer colums = new StringBuffer("(");
        StringBuffer values = new StringBuffer("(");

        for (String field : fieldMap.keySet()) {
            colums.append(field).append(",");
            values.append("?,");
        }

        colums.replace(colums.lastIndexOf(","), colums.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql = sql + colums + " VALUES " + values;

        //values参数
        Object[] params = fieldMap.values().toArray();

        return excuteUpdate(conn, sql, params) == 1;
    }

    /**
     * 更新数据
     */
    public static <T> boolean updateEntity(Class<T> entityClass, Connection conn, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can't update entty : fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuffer colums = new StringBuffer();
        for (String fieldName : fieldMap.keySet()) {
            colums.append(fieldName).append("=?,");
        }
        sql += colums.substring(colums.lastIndexOf(",")) + " WHERE ID=?";

        Object[] params = fieldMap.values().toArray();

        return excuteUpdate(conn, sql, params) == 1;
    }

    /**
     * 删除数据
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, Connection conn, Map<String, Object> fieldMap) {
        if (CollectionUtil.isEmpty(fieldMap)) {
            LOGGER.error("can't delete entity : fieldMap is empty");
            return false;
        }

        String sql = "DELETE FROM " + getTableName(entityClass) + " WHERE ";

        StringBuffer colums = new StringBuffer();
        for (String fieldName : fieldMap.keySet()) {
            colums.append(fieldMap).append("=? AND ");
        }

        sql += colums.substring(colums.indexOf(" AND"));

        Object[] params = fieldMap.values().toArray();

        return excuteUpdate(conn, sql, params) == 1;
    }
}
