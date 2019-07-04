package com.unis.crk.utils;

import com.unis.crk.enums.DataTypeEnum;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.sql.*;
import java.util.*;


/**
 * 1、和数据库建立连接
 * 2、处理DB_Dao传送过来的sql语句，包括增删改查
 * @author pengxl
 * @version 1.0
 * 创建时间: 2019/06/13 9:50
 */
public class JDBC {


    //饿汉模式
    private static JDBC jdbc = new JDBC();
    private String driverClassName;
    private String url;
    private String username;
    private String password;
    private Connection conn;

    /**
     * 单例模式，私有化构造方法
     */
    private JDBC(){}

    //获得JDBC对象的公开方法
    public static JDBC getInstance(){
        return jdbc;
    }

    public JDBC driverClassName(String driverClassName){
        this.driverClassName = driverClassName;
        return this;
    }

    public JDBC url(String url){
        this.url = url;
        return this;
    }

    public JDBC username(String username){
        this.username = username;
        return this;
    }
    public JDBC password(String password){
        this.password = password;
        return this;
    }


    //连接数据库
    private Connection build(){
        try {
            Class.forName(this.driverClassName);
            return DriverManager.getConnection(this.url, this.username, this.password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 增删改操作
     * @param sql
     * @param params
     * @return 返回受影响的行数
     */
    private int DML(String sql,Object...params){
        PreparedStatement ps = null;
        try {
            conn = build();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++){
                ps.setObject(i + 1, params[i]);
            }
            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return 0;
    }


    public int insert(String sql,Object...paras){

        return DML(sql,paras);
    }

    public int update(String sql,Object...paras){

        return DML(sql,paras);
    }

    public int delete(String sql,Object...paras){

        return DML(sql,paras);
    }

    /**
     * 查询操作
     * @param sql
     * @param params
     * @return 返回查询内容为List
     */
    private List<Map<String,Object>> DQL(String sql,Object...params){
        List<Map<String,Object>> lists = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = build();
            ps = conn.prepareStatement(sql);
            for (int i = 0; i < params.length; i++){
                ps.setObject(i + 1, params[i]);
            }
            rs = ps.executeQuery();
            //对结果集进行操作
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            while(rs.next()){
                Map<String,Object> map = new HashMap<String,Object>();
                for(int j=1;j<=columnCount;j++){
                    map.put(rsmd.getColumnName(j), rs.getObject(j));
                }
                lists.add(map);
            }
            return lists;

        } catch (SQLException e) {
            e.printStackTrace();
        }finally{
            if(rs != null){
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(ps != null){
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public List<Map<String,Object>> query(String sql,Object...paras){
        return DQL(sql,paras);
    }


    /**
     * 批量执行DDL语句没有报错就返回执行成功
     * @param sqls
     * @return
     */
    public boolean executeBatchDDLSql(Collection<String> sqls){
        Statement statement = null;
        try {
            conn = build();
            statement = conn.createStatement();
            Iterator<String> sqlIt = sqls.iterator();
            while (sqlIt.hasNext()){
                String sql = sqlIt.next();
                statement.addBatch(sql);
            }
            statement.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally{
            if(statement != null){
                try {
                    statement.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
            if(conn != null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return true;
    }


    /**
     * 验证表是否在数据库存在
     * @param databaseName
     * @param tableName
     * @return
     */
    public boolean verifyTable(String databaseName, String tableName){
        if (StringUtils.isEmpty(databaseName) || StringUtils.isEmpty(tableName)){
            return false;
        }
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.table_name FROM information_schema.TABLES t");
        sql.append(" WHERE t.TABLE_SCHEMA=?");
        sql.append(" AND t.TABLE_NAME=?");
        List<Map<String,Object>> list = DQL(sql.toString(),databaseName,tableName);
        if (ObjectUtils.isEmpty(list)){
            return false;
        }
        return true;
    }


    /**
     * 获取数据库中所有表名
     * @param databaseName
     * @return
     */
    public List<String> getTableNames(String databaseName){
        if (StringUtils.isEmpty(databaseName)){
            return null;
        }
        List<String> tableNames = new ArrayList<>();
        StringBuffer sql = new StringBuffer("");
        sql.append("SELECT t.TABLE_NAME FROM information_schema.TABLES t");
        sql.append(" WHERE t.TABLE_SCHEMA=?");
        List<Map<String,Object>> list = DQL(sql.toString(),databaseName);
        for (Map item : list){
            if(!ObjectUtils.isEmpty(item.get("TABLE_NAME"))){
                tableNames.add(item.get("TABLE_NAME").toString());
            }
        }
        return tableNames;
    }


    /**
     * 获取数据表中表字段信息
     * @param databaseName
     * @param tableName
     * @return
     */
    public List<TableInfoUtil> getTableInfo(String databaseName, String tableName){
        if (StringUtils.isEmpty(databaseName) || StringUtils.isEmpty(tableName)){
            return null;
        }
        StringBuffer sql = new StringBuffer("");
        sql.append(" SELECT c.COLUMN_NAME,c.COLUMN_TYPE,c.COLUMN_COMMENT FROM information_schema.COLUMNS c ");
        sql.append(" WHERE c.TABLE_SCHEMA=?  ");
        sql.append(" AND c.TABLE_NAME=? ");
        List<Map<String,Object>> list = DQL(sql.toString(), databaseName, tableName);
        List<TableInfoUtil> tableInfoUtils = new ArrayList<>();
        for (Map item : list){
            TableInfoUtil tableInfoUtil = new TableInfoUtil();
            // 列名
            tableInfoUtil.setColumnName(ObjectUtils.isEmpty(item.get("COLUMN_NAME")) ? "" : item.get("COLUMN_NAME").toString());
            // 注释
            tableInfoUtil.setRemarks(ObjectUtils.isEmpty(item.get("COLUMN_COMMENT")) ? "" : item.get("COLUMN_COMMENT").toString());
            if (!ObjectUtils.isEmpty(item.get("COLUMN_TYPE"))) {
                String columnType = item.get("COLUMN_TYPE").toString();
                if (columnType.indexOf(",") > -1){
                    String[] strs = columnType.split(",");
                    String[] strs1 = strs[0].split("\\(");
                    // 数据类型
                    tableInfoUtil.setColumnType(DataTypeEnum.getDataTypeValue(strs1[0]));
                    // 数据类型长度
                    tableInfoUtil.setColumnLength(Integer.parseInt(strs1[1]));
                    // 数据类型小数位长度
                    tableInfoUtil.setColumnDecLength(Integer.parseInt(strs[1].replaceAll("\\)","")));
                }else if (columnType.indexOf("(") > -1){
                    String[] strs = columnType.split("\\(");
                    // 数据类型
                    tableInfoUtil.setColumnType(DataTypeEnum.getDataTypeValue(strs[0]));
                    // 数据类型长度
                    tableInfoUtil.setColumnLength(Integer.parseInt(strs[1].replaceAll("\\)","")));
                }else {
                    // 数据类型
                    tableInfoUtil.setColumnType(DataTypeEnum.getDataTypeValue(columnType));
                }
            }
            tableInfoUtils.add(tableInfoUtil);
        }
        return tableInfoUtils;
    }



    public static void main(String[] args) {
//        boolean b = JDBC.getInstance()
//                .driverClassName("com.mysql.cj.jdbc.Driver")
//                .url("jdbc:mysql://127.0.0.1:3306/crk_new?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2b8")
//                .username("root")
//                .password("root")
//                .verifyTable("crk_new","hd_info");
        String str = "double";
        if (str.indexOf(",") > -1){
            String[] strs = str.split(",");
            String[] strs1 = strs[0].split("\\(");
            System.out.println(strs1[0]);
            System.out.println(strs1[1]);
            System.out.println(strs[1].replaceAll("\\)",""));
        }else if (str.indexOf("(") > -1){
            String[] strs = str.split("\\(");
            System.out.println(strs[0]);
            System.out.println(strs[1].replaceAll("\\)",""));
        }else {
            System.out.println(str);
        }
    }






}