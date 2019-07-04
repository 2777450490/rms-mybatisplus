package com.unis.crk.utils;

import com.unis.crk.enums.DataTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/05/28 17:32
 */
public class JDBCUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(JDBCUtil.class);

    // 默认连接参数
    private final static String URL = "jdbc:mysql://127.0.0.1:3306/crk_new?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8";
    private final static String USER_NAME = "root";
    private final static String PASSWORD = "root";
    private final static String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    /**
     * 初始化数据源
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static DataSource initDataSource(String driverClassName, String url, String username, String password) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(url, username, password);
        dataSource.setDriverClassName(driverClassName);
        return dataSource;
    }

    /**
     * 初始化数据源
     * @return
     */
    public static DataSource initDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USER_NAME, PASSWORD);
        dataSource.setDriverClassName(DRIVER_CLASS_NAME);
        return dataSource;
    }


    /**
     * 创建表
     * @param tabName
     * @param tabFields
     * @param isChildrenTable
     * @return
     */
    public static boolean createTable(String tabName, List<TableInfoUtil> tabFields, boolean isChildrenTable) {
        return createTable(tabName, tabFields, isChildrenTable, null);
    }

    /**
     * 创建表
     * @param tabName
     * @param tabFields
     * @param isChildrenTable
     * @return
     */
    public static boolean createTable(String tabName, List<TableInfoUtil> tabFields, boolean isChildrenTable, DataSource dataSource) {
        if (ObjectUtils.isEmpty(tabFields) || StringUtils.isEmpty(tabName)){
            return false;
        }
        // 默认字段
        tabFields.add(new TableInfoUtil("ORG_ID",DataTypeEnum.VARCHAR.getType(),"机构ID",32,0));
        tabFields.add(new TableInfoUtil("CREATER",DataTypeEnum.VARCHAR.getType(),"条目创建人",200,0));
        tabFields.add(new TableInfoUtil("CREATE_TIME",DataTypeEnum.DATETIME.getType(),"条目创建时间",0,0));
        tabFields.add(new TableInfoUtil("EDITOR",DataTypeEnum.VARCHAR.getType(),"最后一次编辑人",200,0));
        tabFields.add(new TableInfoUtil("EDIT_TIME",DataTypeEnum.DATETIME.getType(),"最后一次编辑时间",0,0));
        tabFields.add(new TableInfoUtil("UPLOAD_TIME",DataTypeEnum.DATETIME.getType(),"数据上传时间",0,0));
        tabFields.add(new TableInfoUtil("DT_STATUS",DataTypeEnum.INT.getType(),"数据状态",10,0));

        StringBuffer sql = null;
        ResultSet rs = null;
        Statement ps = null;
        Connection connection = null;
        try {
            if (!ObjectUtils.isEmpty(dataSource)){
                connection = dataSource.getConnection();
            }else {
                connection = initDataSource().getConnection();
            }
            // 开启事务
            connection.setAutoCommit(false);
            ps = connection.createStatement();
            // 判断表是否存在
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            rs = databaseMetaData.getTables(connection.getCatalog(), null, tabName, null);
            if (!rs.next()) { // 不存在
                sql = initTable(tabName, tabFields, isChildrenTable);
            }else { // 存在
                List<TableInfoUtil> list = getTableInfo(connection, tabName);
                sql = alterTableColumn(tabName, list, tabFields,ps);
            }
            if (!ObjectUtils.isEmpty(sql)){
                LOGGER.info("需要执行的SQL：{}",sql.toString());
                String[] sqls = sql.toString().split(";");
                for (String str : sqls){
                    if (!StringUtils.isEmpty(str)){
                        ps.addBatch(str);
                    }
                }
                ps.executeBatch();
            }
            connection.commit();
        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            close(connection, ps, rs);
        }
        return true;
    }


    /**
     * 拼接创建表sql
     * @param tabName
     * @param tabFields
     * @param isChildrenTable
     * @return
     */
    private static StringBuffer initTable(String tabName, List<TableInfoUtil> tabFields, boolean isChildrenTable){
        StringBuffer sql = new StringBuffer("");
        sql.append(" CREATE TABLE ");
        sql.append(tabName);
        sql.append(" (DT_ID varchar(32) PRIMARY KEY NOT NULL COMMENT '主键ID' ");
        if (isChildrenTable){
            sql.append(" ,DT_MAIN_ID varchar(32) NULL COMMENT '主表ID' ");
        }
        // 获取字段名,字段类型
        for (TableInfoUtil item : tabFields){
            sql.append("," + item.getColumnName() + " ");
            sql.append(DataTypeEnum.getDataTypeName(item.getColumnType()));

            if (item.getColumnLength() != null && item.getColumnLength() > 0){
                sql.append("(" + item.getColumnLength());
                sql.append(item.getColumnDecLength() == null || item.getColumnDecLength() < 1 ? ")" : "," + item.getColumnDecLength() + ")");
            }
            String remarks = StringUtils.isEmpty(item.getRemarks()) ? "" : item.getRemarks();
            sql.append(" NULL COMMENT '"+ remarks +"'");
        }
        sql.append(")ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        return sql;
    }



    /**
     * 拼接修改表结构sql 删除或新增字段
     * @param oldFields 原表中字段
     * @param newFields 表中新字段
     * @return
     */
    private static StringBuffer alterTableColumn(String tabName, List<TableInfoUtil> oldFields, List<TableInfoUtil> newFields,Statement ps){
        StringBuffer sql = null;
        if (ObjectUtils.isEmpty(oldFields) || ObjectUtils.isEmpty(newFields)){
            return sql;
        }
        sql = new StringBuffer("");
        int i = 0;
        Set<TableInfoUtil> oldSet = new HashSet<>(oldFields);
        Set<TableInfoUtil> newSet = new HashSet<>(newFields);
        Set<TableInfoUtil> newSet1 = new HashSet<>(newFields);
        newSet.removeAll(oldSet);
        oldSet.removeAll(newSet1);
        if (newSet.size() > 0){
            sql.append("ALTER TABLE " + tabName);
            Iterator<TableInfoUtil> newIt = newSet.iterator();
            while (newIt.hasNext()){ // 遍历需要新增的字段
                if (i > 0){
                    sql.append(",");
                }
                TableInfoUtil item = newIt.next();
                sql.append(" ADD `" + item.getColumnName() + "` ");
                sql.append(DataTypeEnum.getDataTypeName(item.getColumnType()));
                if (item.getColumnLength() != null && item.getColumnLength() > 0){
                    sql.append("(" + item.getColumnLength());
                    sql.append(item.getColumnDecLength() == null || item.getColumnDecLength() < 1 ? ")" : "," + item.getColumnDecLength() + ")");
                }
                String remarks = StringUtils.isEmpty(item.getRemarks()) ? "" : item.getRemarks();
                sql.append(" DEFAULT NULL COMMENT '"+ remarks +"'");
                i++;
            }
            sql.append(";");
        }
        if (oldSet.size() > 0){
            i = 0;
            Iterator<TableInfoUtil> oldIt = oldSet.iterator();
            StringBuffer dropSql = new StringBuffer("");
            while (oldIt.hasNext()){ // 遍历需要删除的字段并排除掉默认字段
                TableInfoUtil item = oldIt.next();
                if (item.getColumnName().equalsIgnoreCase("DT_ID") ||
                        item.getColumnName().equalsIgnoreCase("DT_MAIN_ID")){
                    continue;
                }
                if (i > 0){
                    dropSql.append(",");
                }
                dropSql.append(" DROP COLUMN " + item.getColumnName());
                i++;
            }
            if (dropSql.length() > 0){
                sql.append("ALTER TABLE " + tabName);
                sql.append(dropSql);
                sql.append(";");
            }
        }
        return sql;
    }


    /**
     * 修改表名
     * @param oldTableName
     * @param newTableName
     * @return
     */
    public static boolean alterTable(String oldTableName, String newTableName){
        return alterTable(oldTableName, newTableName , null);
    }


    /**
     * 修改表名
     * @param oldTableName
     * @param newTableName
     * @param dataSource
     * @return
     */
    public static boolean alterTable(String oldTableName, String newTableName , DataSource dataSource){
        if (StringUtils.isEmpty(oldTableName) || StringUtils.isEmpty(newTableName)){
            throw new RuntimeException("传入的TableName为null");
        }
        ResultSet rs = null;
        Statement ps = null;
        Connection connection = null;
        try {
            if (!ObjectUtils.isEmpty(dataSource)){
                connection = dataSource.getConnection();
            }else {
                connection = initDataSource().getConnection();
            }
            ps = connection.createStatement();
            // 判断表是否存在
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            rs = databaseMetaData.getTables(connection.getCatalog(), null, oldTableName, null);
            if (!rs.next()) { // 不存在
                return false;
            }
            String sql = "ALTER TABLE "+ oldTableName +" RENAME TO " + newTableName;
            LOGGER.info("需要执行的SQL：{}", sql);
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(connection, ps, rs);
        }
        return true;
    }

    /**
     * 批量执行DDL语句sql
     * @param sqls
     * @return
     */
    public static boolean executeBatchDDLSql(Set<String> sqls) {
        return executeBatchDDLSql(null, sqls);
    }


    /**
     * 批量执行DDL语句sql
     * @param dataSource
     * @param sqls
     * @return
     */
    public static boolean executeBatchDDLSql(DataSource dataSource, Set<String> sqls) {
        if (ObjectUtils.isEmpty(sqls)){
            throw new RuntimeException("传入的sql为null");
        }
        Statement ps = null;
        Connection connection = null;
        try {
            if (!ObjectUtils.isEmpty(dataSource)){
                connection = dataSource.getConnection();
            }else {
                connection = initDataSource().getConnection();
            }
            ps = connection.createStatement();
            Iterator<String> sqlIt = sqls.iterator();
            while (sqlIt.hasNext()){
                String sql = sqlIt.next();
                LOGGER.info("需要执行的SQL：{}", sql);
                ps.addBatch(sql);
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(connection, ps , null);
        }
        return true;
    }


    /**
     * 单条执行DDL语句sql
     * @param dataSource
     * @param sql
     * @return
     */
    public static boolean executeDDLSql(DataSource dataSource, String sql) {
        if (StringUtils.isEmpty(sql)){
            throw new RuntimeException("传入的sql为null");
        }
        Statement ps = null;
        Connection connection = null;
        try {
            if (!ObjectUtils.isEmpty(dataSource)){
                connection = dataSource.getConnection();
            }else {
                connection = initDataSource().getConnection();
            }
            ps = connection.createStatement();
            LOGGER.info("需要执行的SQL：{}", sql);
            ps.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            close(connection, ps , null);
        }
        return true;
    }

    /**
     * 单条执行DDL语句sql
     * @param sql
     * @return
     */
    public static boolean executeDDLSql(String sql) {
        return executeDDLSql(null, sql);
    }

    /**
     * 判断是否连接成功
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static boolean isConnect(String driverClassName, String url, String username, String password) {
        Connection connection = null;
        try {
            connection = initDataSource(driverClassName, url, username, password).getConnection();
            if (!connection.isClosed()){
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
            close(connection);
        }
        return false;
    }


    /**
     * 获取当前连接下的数据库中所有表名
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @return
     */
    public static List<String> getTableNames(String driverClassName, String url, String username, String password) {
        Connection connection = null;
        ResultSet rs = null;
        List<String> list = new ArrayList<>();
        try {
            connection = initDataSource(driverClassName, url, username, password).getConnection();
            // FIXME: 2019/6/3 待判断数据库类型，不同数据库getTables参数不同
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            //获取表名的结果集
            rs = databaseMetaData.getTables(connection.getCatalog(), null, null, new String[]{"TABLE"});
            while(rs.next()){
                String tableName = rs.getString("TABLE_NAME");
                list.add(tableName);
            }
        }catch (SQLException e){
            LOGGER.info("getTableNames--->Exception:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }finally {
            close(connection, null, rs);
        }
        return list;
    }

    /**
     * 获取指定数据库中表中的 列名、类型、注释
     * @param driverClassName
     * @param url
     * @param username
     * @param password
     * @param tableName
     * @return
     */
    public static List<TableInfoUtil> getTableInfo(String driverClassName, String url, String username, String password, String tableName) {
        List<TableInfoUtil> list = new ArrayList<>();
        Connection connection = null;
        ResultSet result = null;
        try {
            connection = initDataSource(driverClassName, url, username, password).getConnection();
            // FIXME: 2019/6/3 待判断数据库类型，不同数据库getColumns参数不同
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            result = databaseMetaData.getColumns(connection.getCatalog(), null, tableName, null);
            while(result.next()){
                TableInfoUtil tableInfo = new TableInfoUtil();
                // 列名
                tableInfo.setColumnName(result.getString("COLUMN_NAME"));
                // 类型
                tableInfo.setColumnType(DataTypeEnum.getDataTypeValue(result.getString("TYPE_NAME")));
                // 注释
                tableInfo.setRemarks(result.getString("REMARKS"));
                // 数据类型长度
                tableInfo.setColumnLength(result.getInt("COLUMN_SIZE"));

                list.add(tableInfo);
            }
        }catch (SQLException e){
            LOGGER.info("getTableInfo--->Exception:{}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        } finally {
            close(connection, null, result);
        }
        return list;
    }



    /**
     * 获取指定数据库中表中的 列名、类型、注释
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    private static List<TableInfoUtil> getTableInfo(Connection connection, String tableName) throws SQLException {
        // FIXME: 2019/6/3 待判断数据库类型，不同数据库getColumns参数不同
        DatabaseMetaData databaseMetaData = connection.getMetaData();
        ResultSet result = databaseMetaData.getColumns(connection.getCatalog(), null, tableName, null);
        List<TableInfoUtil> list = new ArrayList<>();
        while(result.next()){
            list.add(new TableInfoUtil(
                    result.getString("COLUMN_NAME"), // 列名
                    DataTypeEnum.getDataTypeValue(result.getString("TYPE_NAME")), // 类型
                    result.getString("REMARKS"), // 注释
                    result.getInt("COLUMN_SIZE"), // 数据类型长度
                    0));
        }
        return list;
    }



    /**
     * 关闭数据库连接
     * @param connection
     */
    public static void close(Connection connection){
        if (!ObjectUtils.isEmpty(connection)){
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("关闭数据库连接失败：{}", e.getMessage());
                e.printStackTrace();
            }
        }
    }


    /**
     * 关闭连接
     * @param connection
     * @param statement
     * @param resultSet
     */
    public static void close(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (!ObjectUtils.isEmpty(resultSet)) {
                resultSet.close();
            }
        } catch (SQLException ResultE) {
            ResultE.printStackTrace();
            LOGGER.info("ResultSetClose--->Exception:{}", ResultE.getMessage());
        } finally {
            try {
                if (!ObjectUtils.isEmpty(statement)) {
                    statement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.info("StatementClose--->Exception:{}", e.getMessage());
            } finally {
                close(connection);
            }
        }
    }

    /**
     * 关闭连接
     * @param connection
     * @param preparedStatement
     * @param resultSet
     */
    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (!ObjectUtils.isEmpty(resultSet)) {
                resultSet.close();
            }
        } catch (SQLException ResultE) {
            ResultE.printStackTrace();
            LOGGER.info("ResultSetClose--->Exception:{}", ResultE.getMessage());
        } finally {
            try {
                if (!ObjectUtils.isEmpty(preparedStatement)) {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.info("PreparedStatementClose--->Exception:{}", e.getMessage());
            } finally {
                close(connection);
            }
        }
    }




    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        List<TableInfoUtil> list = JDBCUtil.getTableInfo(DRIVER_CLASS_NAME, URL, USER_NAME, PASSWORD, "dt_test");
        System.out.println(list.size());
        list.forEach(System.out::println);
    }



}
